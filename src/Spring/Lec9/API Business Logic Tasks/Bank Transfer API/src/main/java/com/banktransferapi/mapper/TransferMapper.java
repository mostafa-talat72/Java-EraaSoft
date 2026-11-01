package com.banktransferapi.mapper;

import com.banktransferapi.dto.TransferDTO;
import com.banktransferapi.dto.TransferRequestDTO;
import com.banktransferapi.model.Transfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

// AccountMapper provides the sender/receiver -> AccountSimpleDTO conversion.
@Mapper(componentModel = "spring", uses = AccountMapper.class)
public interface TransferMapper {

    TransferDTO convertFromTransferToTransferDto(Transfer transfer);

    List<TransferDTO> convertFromTransferListToTransferDtoList(List<Transfer> transfers);

    // senderAccountId and receiverAccountId are resolved to Account entities
    // by the service, and createdAt is filled by @PrePersist.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "receiver", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Transfer convertFromTransferRequestDtoToTransfer(TransferRequestDTO requestDTO);
}
