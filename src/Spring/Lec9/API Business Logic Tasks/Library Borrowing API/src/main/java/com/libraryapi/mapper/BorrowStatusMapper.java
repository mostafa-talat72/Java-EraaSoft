package com.libraryapi.mapper;

import com.libraryapi.dto.BorrowStatusDTO;
import com.libraryapi.dto.BorrowStatusRequestDTO;
import com.libraryapi.model.BorrowStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BorrowStatusMapper {

    BorrowStatusDTO convertFromBorrowStatusToBorrowStatusDto(BorrowStatus borrowStatus);

    List<BorrowStatusDTO> convertFromBorrowStatusListToBorrowStatusDtoList(List<BorrowStatus> borrowStatuses);

    // id comes from the sequence, never from the request.
    @Mapping(target = "id", ignore = true)
    BorrowStatus convertFromBorrowStatusRequestDtoToBorrowStatus(BorrowStatusRequestDTO requestDTO);

    // In-place update; the entity is managed, so dirty checking flushes it.
    @Mapping(target = "id", ignore = true)
    void updateBorrowStatusFromBorrowStatusRequestDto(BorrowStatusRequestDTO requestDTO,
                                                      @MappingTarget BorrowStatus borrowStatus);
}
