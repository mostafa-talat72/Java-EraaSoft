package com.bank_security.mapper;

import com.bank_security.dto.TransferStatusDTO;
import com.bank_security.dto.TransferStatusSimpleDTO;
import com.bank_security.model.TransferStatus;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransferStatusMapper {

    public TransferStatus convertFromTransferStatusDtoToTransferStatus(TransferStatusDTO transferStatusDTO);

    public TransferStatusDTO convertFromTransferStatusToTransferStatusDto(TransferStatus transferStatus);

    public List<TransferStatus> convertFromTransferStatusDtoListToTransferStatusList(List<TransferStatusDTO> transferStatusDtoList);

    public List<TransferStatusDTO> convertFromTransferStatusListToTransferStatusDtoList(List<TransferStatus> transferStatuses);

    public TransferStatus convertFromTransferStatusSimpleDtoToTransferStatus(TransferStatusSimpleDTO transferStatusSimpleDTO);

    public TransferStatusSimpleDTO convertFromTransferStatusToTransferStatusSimpleDto(TransferStatus transferStatus);

    public List<TransferStatus> convertFromTransferStatusSimpleDtoListToTransferStatusList(List<TransferStatusSimpleDTO> transferStatusSimpleDtoList);

    public List<TransferStatusSimpleDTO> convertFromTransferStatusListToTransferStatusSimpleDtoList(List<TransferStatus> transferStatuses);

}
