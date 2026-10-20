package com.bank_security.mapper;

import com.bank_security.dto.AccountStatusDTO;
import com.bank_security.dto.AccountStatusSimpleDTO;
import com.bank_security.model.AccountStatus;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountStatusMapper {

    public AccountStatus convertFromAccountStatusDtoToAccountStatus(AccountStatusDTO accountStatusDTO);

    public AccountStatusDTO convertFromAccountStatusToAccountStatusDto(AccountStatus accountStatus);

    public List<AccountStatus> convertFromAccountStatusDtoListToAccountStatusList(List<AccountStatusDTO> accountStatusDtoList);

    public List<AccountStatusDTO> convertFromAccountStatusListToAccountStatusDtoList(List<AccountStatus> accountStatuses);

    public AccountStatus convertFromAccountStatusSimpleDtoToAccountStatus(AccountStatusSimpleDTO accountStatusSimpleDTO);

    public AccountStatusSimpleDTO convertFromAccountStatusToAccountStatusSimpleDto(AccountStatus accountStatus);

    public List<AccountStatus> convertFromAccountStatusSimpleDtoListToAccountStatusList(List<AccountStatusSimpleDTO> accountStatusSimpleDtoList);

    public List<AccountStatusSimpleDTO> convertFromAccountStatusListToAccountStatusSimpleDtoList(List<AccountStatus> accountStatuses);

}
