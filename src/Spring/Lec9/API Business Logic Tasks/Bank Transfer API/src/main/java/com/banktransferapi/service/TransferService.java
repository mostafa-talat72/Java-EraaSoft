package com.banktransferapi.service;

import com.banktransferapi.dto.TransferDTO;
import com.banktransferapi.dto.TransferRequestDTO;
import com.banktransferapi.model.Transfer;
import jakarta.validation.Valid;

import java.util.List;

public interface TransferService {

    // @Valid on the contract, not on the implementation: Hibernate Validator
    // forbids an override from redefining the parameter constraints (HV000151).
    TransferDTO transfer(@Valid TransferRequestDTO requestDTO);

    List<TransferDTO> getAllTransfers();

    TransferDTO getTransferById(Long id);

    Transfer getTransferEntityById(Long id);

    List<TransferDTO> getTransfersBySender(Long accountId);

    List<TransferDTO> getTransfersByReceiver(Long accountId);
}
