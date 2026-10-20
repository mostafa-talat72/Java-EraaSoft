package com.bank_security.service.impl;

import com.bank_security.exception.CustomException;
import com.bank_security.mapper.TransferStatusMapper;
import com.bank_security.model.TransferStatus;
import com.bank_security.repo.TransferStatusRepo;
import com.bank_security.service.TransferStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferStatusServiceImpl implements TransferStatusService {

    private TransferStatusRepo transferStatusRepo;

    private TransferStatusMapper transferStatusMapper;

    @Autowired
    public TransferStatusServiceImpl(TransferStatusRepo transferStatusRepo, TransferStatusMapper transferStatusMapper) {
        this.transferStatusRepo = transferStatusRepo;
        this.transferStatusMapper = transferStatusMapper;
    }


    @Override
    public TransferStatus getTransferStatusEntityByName(String name) {
        return transferStatusRepo.findByName(name).orElseThrow(
                ()->  new CustomException(
                        "Status Name not found Exception",
                        "Transfer Status not found with this name: " + name
                )
        );
    }
}
