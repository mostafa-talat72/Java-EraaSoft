package com.banktransferapi.service;

import com.banktransferapi.dto.TransferDTO;
import com.banktransferapi.dto.TransferRequestDTO;
import com.banktransferapi.model.Transfer;

import java.util.List;

public interface TransferService {

    TransferDTO transfer(TransferRequestDTO requestDTO);

    List<TransferDTO> getAllTransfers();

    TransferDTO getTransferById(Long id);

    Transfer getTransferEntityById(Long id);

    List<TransferDTO> getTransfersBySender(Long accountId);

    List<TransferDTO> getTransfersByReceiver(Long accountId);
}
