package com.banktransferapi.service.impl;

import com.banktransferapi.dto.TransferDTO;
import com.banktransferapi.dto.TransferRequestDTO;
import com.banktransferapi.exception.BadRequestException;
import com.banktransferapi.exception.NotFoundException;
import com.banktransferapi.helper.AccountHelper;
import com.banktransferapi.mapper.TransferMapper;
import com.banktransferapi.model.Account;
import com.banktransferapi.model.Transfer;
import com.banktransferapi.repo.TransferRepo;
import com.banktransferapi.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepo transferRepo;
    private final TransferMapper transferMapper;

    // Account access goes through AccountHelper, which depends on repositories only,
    // so this service never depends on AccountService and there is no injection cycle.
    private final AccountHelper accountHelper;


    @Transactional
    @Override
    public TransferDTO transfer(TransferRequestDTO requestDTO) {
        // Rule 1: money must move between two different accounts.
        if(requestDTO.getSenderAccountId().equals(requestDTO.getReceiverAccountId())){
            throw new BadRequestException("Sender and receiver must be different accounts");
        }

        // Lock both rows before any check. Ascending id order keeps opposite
        // concurrent transfers (A -> B and B -> A) from deadlocking each other.
        Long senderId = requestDTO.getSenderAccountId();
        Long receiverId = requestDTO.getReceiverAccountId();

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
        if(requestDTO.getAmount().compareTo(sender.getBalance()) > 0){
            throw new BadRequestException("Insufficient balance");
        }

        // Rule 4: daily window is [today 00:00, tomorrow 00:00).
        LocalDateTime start = LocalDate.now().atStartOfDay();

        LocalDateTime end = start.plusDays(1);

        // Sum of today's outgoing transfers; the query's coalesce returns 0 when empty.
        BigDecimal usedToDay = transferRepo.sumAmountBySenderAndPeriod(requestDTO.getSenderAccountId()
                , start, end);

        // The limit belongs to the SENDER's account type; reaching it exactly is allowed.
        if(requestDTO.getAmount().add(usedToDay).compareTo(sender.getAccountType().getDailyLimit()) > 0){
            throw new BadRequestException(
                    "Daily transfer limit exceeded (limit " + sender.getAccountType().getDailyLimit()
                            + ", used today " + usedToDay
                            + ", requested " + requestDTO.getAmount() + ")");
        }




        Transfer transfer = transferMapper.convertFromTransferRequestDtoToTransfer(requestDTO);

        sender.setBalance(sender.getBalance().subtract(requestDTO.getAmount()));

        receiver.setBalance(receiver.getBalance().add(requestDTO.getAmount()));


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

    @Override
    public TransferDTO getTransferById(Long id) {
        // 404 when the transfer does not exist; entity -> DTO afterwards.
        Transfer transfer = findTransferOrThrow(id);

        return transferMapper.convertFromTransferToTransferDto(transfer);
    }

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

    // Shared helper for safe entity retrieval with consistent 404 messaging.
    private Transfer findTransferOrThrow(Long id) {
        return transferRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Transfer with id " + id + " not found"));
    }
}
