package com.banktransferapi.mapper;

import com.banktransferapi.dto.AccountDTO;
import com.banktransferapi.dto.AccountRequestDTO;
import com.banktransferapi.dto.AccountSimpleDTO;
import com.banktransferapi.dto.AccountUpdateRequestDTO;
import com.banktransferapi.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

// AccountTypeMapper is used so the nested AccountType -> AccountTypeSimpleDTO
// conversion happens automatically inside AccountDTO.
@Mapper(componentModel = "spring", uses = AccountTypeMapper.class)
public interface AccountMapper {

    AccountDTO convertFromAccountToAccountDto(Account account);

    AccountSimpleDTO convertFromAccountToAccountSimpleDto(Account account);

    List<AccountDTO> convertFromAccountListToAccountDtoList(List<Account> accounts);

    // accountTypeId is only an id: the service resolves it to a real AccountType,
    // and the opening balance becomes the starting balance.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "balance", source = "openingBalance")
    @Mapping(target = "accountType", ignore = true)
    @Mapping(target = "transfersSent", ignore = true)
    @Mapping(target = "transfersRecipient", ignore = true)
    Account convertFromAccountRequestDtoToAccount(AccountRequestDTO requestDTO);

    // accountNumber and balance must survive an update untouched;
    // the account type is resolved by the service from accountTypeId.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "accountType", ignore = true)
    @Mapping(target = "transfersSent", ignore = true)
    @Mapping(target = "transfersRecipient", ignore = true)
    void updateAccountFromAccountUpdateRequestDto(AccountUpdateRequestDTO requestDTO,
                                                  @MappingTarget Account account);
}
