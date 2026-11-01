package com.banktransferapi.mapper;

import com.banktransferapi.dto.AccountTypeDTO;
import com.banktransferapi.dto.AccountTypeRequestDTO;
import com.banktransferapi.dto.AccountTypeSimpleDTO;
import com.banktransferapi.model.AccountType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountTypeMapper {

    AccountTypeDTO convertFromAccountTypeToAccountTypeDto(AccountType accountType);

    AccountTypeSimpleDTO convertFromAccountTypeToAccountTypeSimpleDto(AccountType accountType);

    List<AccountTypeDTO> convertFromAccountTypeListToAccountTypeDtoList(List<AccountType> accountTypes);

    AccountType convertFromAccountTypeRequestDtoToAccountType(AccountTypeRequestDTO requestDTO);

    // The id is never taken from the payload: on PUT it is already in the URL.
    @Mapping(target = "id", ignore = true)
    void updateAccountTypeFromAccountTypeRequestDto(AccountTypeRequestDTO requestDTO,
                                                    @MappingTarget AccountType accountType);
}
