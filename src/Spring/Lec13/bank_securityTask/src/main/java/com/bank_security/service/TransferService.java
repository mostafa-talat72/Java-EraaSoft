package com.bank_security.service;

import com.bank_security.dto.TransferDTO;
import com.bank_security.dto.TransferSimpleDTO;
import com.bank_security.helper.CustomUserDetails;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

// Service contract for Transfer.
public interface TransferService {

    @PreAuthorize("hasAuthority('TRANSFER_CREATE')")
    public TransferDTO createTransfer(TransferSimpleDTO transferSimpleDTO, CustomUserDetails customUserDetails);

    @PreAuthorize("hasAuthority('TRANSFER_READ')")
    public TransferDTO getTransferById(long id, CustomUserDetails customUserDetails) ;

    @PreAuthorize("hasAuthority('TRANSFER_APPROVE')")
    public TransferDTO approveTransferById(long id, CustomUserDetails customUserDetails);

    @PreAuthorize("hasAuthority('REPORT_VIEW')")
    public List<TransferDTO> getAllTransfers();
}
