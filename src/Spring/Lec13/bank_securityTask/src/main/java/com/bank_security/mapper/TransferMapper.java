package com.bank_security.mapper;

import com.bank_security.dto.TransferDTO;
import com.bank_security.dto.TransferSimpleDTO;
import com.bank_security.model.Transfer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransferMapper {

    // All nested relations are slim (no back-references), so no ignore needed.
    public Transfer convertFromTransferDtoToTransfer(TransferDTO transferDTO);

    public TransferDTO convertFromTransferToTransferDto(Transfer transfer);

    public List<Transfer> convertFromTransferDtoListToTransferList(List<TransferDTO> transferDtoList);

    public List<TransferDTO> convertFromTransferListToTransferDtoList(List<Transfer> transfers);

    public Transfer convertFromTransferSimpleDtoToTransfer(TransferSimpleDTO transferSimpleDTO);

    public TransferSimpleDTO convertFromTransferToTransferSimpleDto(Transfer transfer);

    public List<Transfer> convertFromTransferSimpleDtoListToTransferList(List<TransferSimpleDTO> transferSimpleDtoList);

    public List<TransferSimpleDTO> convertFromTransferListToTransferSimpleDtoList(List<Transfer> transfers);

}
