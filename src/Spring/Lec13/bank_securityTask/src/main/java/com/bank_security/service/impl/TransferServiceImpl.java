package com.bank_security.service.impl;

import com.bank_security.dto.TransferDTO;
import com.bank_security.dto.TransferSimpleDTO;
import com.bank_security.exception.CustomException;
import com.bank_security.helper.CustomUserDetails;
import com.bank_security.mapper.TransferMapper;
import com.bank_security.model.Account;
import com.bank_security.model.Customer;
import com.bank_security.model.Transfer;
import com.bank_security.repo.TransferRepo;
import com.bank_security.service.AccountService;
import com.bank_security.service.CustomerService;
import com.bank_security.service.TransferService;
import com.bank_security.service.TransferStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class TransferServiceImpl implements TransferService {

    private TransferRepo transferRepo;

    private TransferMapper transferMapper;

    private AccountService accountService;

    private CustomerService customerService;

    private TransferStatusService transferStatusService;

    @Autowired
    public TransferServiceImpl(TransferRepo transferRepo, TransferMapper transferMapper, AccountService accountService, CustomerService customerService, TransferStatusService transferStatusService) {
        this.transferRepo = transferRepo;
        this.transferMapper = transferMapper;
        this.accountService = accountService;
        this.customerService = customerService;
        this.transferStatusService = transferStatusService;
    }


    @Override
    public TransferDTO createTransfer(TransferSimpleDTO transferSimpleDTO, CustomUserDetails customUserDetails) {

        if(Objects.nonNull(transferSimpleDTO.getId())){
            throw new CustomException(
                    "Id non null Exception",
                    "Id must be null to create transfer"
            );
        }

        Customer customer = customerService.getCustomerEntityByEmail(customUserDetails.getUsername());


        if(Objects.isNull(customer.getAccount())){
            throw new CustomException(
                    "Source Account Not found Exception",
                    "You don't have a bank account. Contact an admin to create one"
            );
        }

        Account sourceAccount = customer.getAccount();

        if(sourceAccount.getAccountNumber().equals(transferSimpleDTO.getToAccount().getAccountNumber())){
            throw new CustomException(
                    "Source and destination Exception",
                    "Source and destination accounts cannot be the same"
            );
        }

        Account destinationAccount = accountService.getAccountEntityByAccountNumber(transferSimpleDTO.getToAccount().getAccountNumber());



        if(sourceAccount.getBalance().compareTo(transferSimpleDTO.getAmount()) < 0){
            throw new CustomException(
                    "Balance not enough Exception",
                    "Source account balance is not enough to complete this transfer"
            );
        }

        if(!sourceAccount.getStatus().getName().equals("ACTIVE")){
            throw new CustomException(
                    "Not Active Exception",
                    "Source Account does not Active"
            );
        }



        if(!destinationAccount.getStatus().getName().equals("ACTIVE")){
            throw new CustomException(
                    "Not Active Exception",
                    "Destination Account does not Active"
            );
        }

        Transfer transfer = transferMapper.convertFromTransferSimpleDtoToTransfer(transferSimpleDTO);

        transfer.setCreatedBy(customer);
        transfer.setFromAccount(sourceAccount);
        transfer.setToAccount(destinationAccount);
        transfer.setStatus(
                transferStatusService.getTransferStatusEntityByName("PENDING")
        );



        return transferMapper.convertFromTransferToTransferDto(
                transferRepo.save(transfer)
        );
    }

    @Override
    public TransferDTO getTransferById(long id,  CustomUserDetails customUserDetails) {
        Transfer transfer = transferRepo.findById(id).orElseThrow(
                () ->  new CustomException(
                        "Id Not found Exception",
                        "Transfer not found with this id: " + id
                )
        );

        boolean privileged = customUserDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                        || a.getAuthority().equals("ROLE_BANK_MANAGER")
                        || a.getAuthority().equals("ROLE_AUDITOR"));

        if (!privileged) {
            if (Objects.isNull(transfer.getCreatedBy())
                    || !transfer.getCreatedBy().getEmail().equals(customUserDetails.getUsername())) {
                throw new AccessDeniedException("Access denied: this transfer does not belong to you");
            }
        }

        return transferMapper.convertFromTransferToTransferDto(transfer);
    }

    @Override
    @Transactional
    public TransferDTO approveTransferById(long id,  CustomUserDetails customUserDetails) {
        Transfer transfer = transferRepo.findById(id).orElseThrow(
                () ->  new CustomException(
                        "Id Not found Exception",
                        "Transfer not found with this id: " + id
                )
        );

        if(!transfer.getStatus().getName().equals("PENDING")){
            throw new CustomException(
                    "Invalid Transfer State Exception",
                    "Only PENDING transfers can be approved"
            );
        }

        transfer.setStatus(
                transferStatusService.getTransferStatusEntityByName("APPROVED")
        );

        Account sourceAccount = transfer.getFromAccount();

        if(!sourceAccount.getStatus().getName().equals("ACTIVE")){
            transfer.setStatus(
                    transferStatusService.getTransferStatusEntityByName("REJECTED")
            );

            transferRepo.save(transfer);

            throw new CustomException(
                    "Not Active Exception",
                    "Source Account does not Active"
            );
        }


        Account destinationAccount = transfer.getToAccount();

        if(!destinationAccount.getStatus().getName().equals("ACTIVE")){
            transfer.setStatus(
                    transferStatusService.getTransferStatusEntityByName("REJECTED")
            );

            transferRepo.save(transfer);

            throw new CustomException(
                    "Not Active Exception",
                    "Destination Account does not Active"
            );
        }

        if(sourceAccount.getBalance().compareTo(transfer.getAmount()) < 0){

            transfer.setStatus(
                    transferStatusService.getTransferStatusEntityByName("REJECTED")
            );

            transferRepo.save(transfer);

            throw new CustomException(
                    "Balance not enough Exception",
                    "Source account balance is not enough to complete this transfer"
            );
        }

        transfer.setStatus(
                transferStatusService.getTransferStatusEntityByName("APPROVED")
        );

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(transfer.getAmount()));

        destinationAccount.setBalance(destinationAccount.getBalance().add(transfer.getAmount()));

        transfer.setStatus(
                transferStatusService.getTransferStatusEntityByName("COMPLETED")
        );

        return transferMapper.convertFromTransferToTransferDto(
                transferRepo.save(transfer)
        );
    }

    @Override
    public List<TransferDTO> getAllTransfers() {
        return transferMapper.convertFromTransferListToTransferDtoList(
                transferRepo.findAll()
        );
    }
}
