package com.banktransferapi.service.impl;

import com.banktransferapi.config.BankTransferProperties;
import com.banktransferapi.dto.TransferDTO;
import com.banktransferapi.dto.TransferRequestDTO;
import com.banktransferapi.exception.BadRequestException;
import com.banktransferapi.exception.NotFoundException;
import com.banktransferapi.exception.VerificationRequiredException;
import com.banktransferapi.helper.AccountHelper;
import com.banktransferapi.mapper.TransferMapper;
import com.banktransferapi.model.Account;
import com.banktransferapi.model.Transfer;
import com.banktransferapi.repo.TransferRepo;
import com.banktransferapi.service.TransferService;
import com.banktransferapi.verification.VerificationCodeStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepo transferRepo;
    private final TransferMapper transferMapper;

    // Account access goes through AccountHelper, which depends on repositories only,
    // so this service never depends on AccountService and there is no injection cycle.
    private final AccountHelper accountHelper;

    // Rule 5 collaborator: the threshold settings and the pending-code store.
    private final BankTransferProperties transferProperties;
    private final VerificationCodeStore verificationStore;


    @Override
    public TransferDTO transfer(TransferRequestDTO requestDTO) {
        // Input validation (required ids, amount >= 0.01, at most 2 decimals) is
        // declared once on TransferRequestDTO and applied by @Valid on TransferService;
        // that covers direct/internal calls too, not only the HTTP endpoint.
        Long senderId = requestDTO.getSenderAccountId();
        Long receiverId = requestDTO.getReceiverAccountId();
        BigDecimal amount = requestDTO.getAmount();

        // Rule 1: money must move between two different accounts.
        if (senderId.equals(receiverId)){
            throw new BadRequestException("Sender and receiver must be different accounts");
        }

        // Lock both rows before any check. Ascending id order keeps opposite
        // concurrent transfers (A -> B and B -> A) from deadlocking each other.
        Account sender;
        Account receiver;

        if (Long.compare(senderId, receiverId) < 0) {
            sender = accountHelper.lockOrThrow(senderId);
            receiver = accountHelper.lockOrThrow(receiverId);
        } else {
            receiver = accountHelper.lockOrThrow(receiverId);
            sender = accountHelper.lockOrThrow(senderId);
        }

        // Rule 3: both sides must be active.
        if(!sender.getActive()){
            throw new BadRequestException("Sender account is not active");
        }

        if(!receiver.getActive()){
            throw new BadRequestException("Receiver account is not active");
        }

        // Rule 2: the sender must cover the amount; amount == balance is allowed (zero left).
        if(amount.compareTo(sender.getBalance()) > 0){
            throw new BadRequestException("Insufficient balance");
        }

        // One timestamp for the whole request: the [today 00:00, tomorrow 00:00)
        // window and Transfer.createdAt are derived from it, so a transfer that is
        // flushed just after midnight cannot be counted in the wrong day's limit.
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.toLocalDate().atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        // Sum of today's outgoing transfers; the query's coalesce returns 0 when empty.
        BigDecimal usedToday = transferRepo.sumAmountBySenderAndPeriod(senderId, start, end);

        if (usedToday == null) {
            usedToday = BigDecimal.ZERO;
        }

        // Rule 4: the limit belongs to the SENDER's account type;
        // reaching it exactly is allowed.
        if(amount.add(usedToday).compareTo(sender.getAccountType().getDailyLimit()) > 0){
            throw new BadRequestException(
                    "Daily transfer limit exceeded (limit " + sender.getAccountType().getDailyLimit()
                            + ", used today " + usedToday
                            + ", requested " + amount + ")");
        }

        // Rule 5: large transfers need a code. This runs before any balance change,
        // so an unverified request leaves both accounts untouched.
        if (requiresVerification(amount)) {
            verifyLargeTransfer(senderId, receiverId, amount, requestDTO.getVerificationCode());
        }

        Transfer transfer = transferMapper.convertFromTransferRequestDtoToTransfer(requestDTO);

        // Explicit timestamp (instead of letting @PrePersist pick one at flush time)
        // keeps the record inside the same day the limit check used.
        transfer.setCreatedAt(now);

        sender.setBalance(sender.getBalance().subtract(amount));

        receiver.setBalance(receiver.getBalance().add(amount));

        transfer.setSender(sender);
        transfer.setReceiver(receiver);

        Transfer saved = transferRepo.save(transfer);

        return transferMapper.convertFromTransferToTransferDto(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransferDTO> getAllTransfers() {

        // to avoid N+1 selects and get a deterministic newest-first order.
        List<Transfer> transfers = transferRepo.findAllByOrderByCreatedAtDesc();

        // Same convention as the other list endpoints: an empty list is reported as 404.
        if(transfers.isEmpty()){
            throw new NotFoundException("No transfers found in the system");
        }

        return transferMapper.convertFromTransferListToTransferDtoList(transfers);
    }

    @Transactional(readOnly = true)
    @Override
    public TransferDTO getTransferById(Long id) {
        // 404 when the transfer does not exist; entity -> DTO afterwards.
        Transfer transfer = findTransferOrThrow(id);

        return transferMapper.convertFromTransferToTransferDto(transfer);
    }

    @Transactional(readOnly = true)
    @Override
    public Transfer getTransferEntityById(Long id) {
        // Raw entity for internal callers.
        return findTransferOrThrow(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransferDTO> getTransfersBySender(Long accountId) {
        // Newest first, sender+receiver fetched with the query's @EntityGraph.
        List<Transfer> transfers = transferRepo.findBySenderIdOrderByCreatedAtDesc(accountId);

        if(transfers.isEmpty()){
            throw new NotFoundException("No transfers found for sender account with id " + accountId);
        }
        return transferMapper.convertFromTransferListToTransferDtoList(transfers);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransferDTO> getTransfersByReceiver(Long accountId) {
        // Newest first, sender+receiver fetched with the query's @EntityGraph.
        List<Transfer> transfers = transferRepo.findByReceiverIdOrderByCreatedAtDesc(accountId);

        if(transfers.isEmpty()){
            throw new NotFoundException("No transfers found for receiver account with id " + accountId);
        }
        return transferMapper.convertFromTransferListToTransferDtoList(transfers);
    }

    private boolean requiresVerification(BigDecimal amount) {
        // "Above the threshold" is strict: exactly the threshold needs no code.
        return amount.compareTo(transferProperties.getVerificationThreshold()) > 0;
    }

    // Step 1 without a code: issue one and reject the request.
    // Step 2 with a code: it must match (and is consumed inside the store).
    private void verifyLargeTransfer(Long senderId, Long receiverId, BigDecimal amount, String verificationCode) {
        if (verificationCode == null || verificationCode.isBlank()) {
            verificationStore.create(senderId, receiverId, amount);

            throw new VerificationRequiredException(
                    "Transfer above " + transferProperties.getVerificationThreshold()
                            + " requires a verification code. A code has been generated (simulated SMS in the application log); "
                            + "repeat the same request with it in 'verificationCode'");
        }

        if (!verificationStore.matches(senderId, receiverId, amount, verificationCode)) {
            throw new BadRequestException("Invalid or already used verification code");
        }
    }

    // Shared helper for safe entity retrieval with consistent 404 messaging.
    private Transfer findTransferOrThrow(Long id) {
        return transferRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Transfer with id " + id + " not found"));
    }
}
