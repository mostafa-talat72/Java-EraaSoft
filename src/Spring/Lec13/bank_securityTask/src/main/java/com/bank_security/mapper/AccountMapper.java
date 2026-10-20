package com.bank_security.mapper;

import com.bank_security.dto.AccountDTO;
import com.bank_security.dto.AccountSimpleDTO;
import com.bank_security.model.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    // Nested customer is slim (no back-reference), so no ignore needed.
    public Account convertFromAccountDtoToAccount(AccountDTO accountDTO);

    public AccountDTO convertFromAccountToAccountDto(Account account);

    public List<Account> convertFromAccountDtoListToAccountList(List<AccountDTO> accountDtoList);

    public List<AccountDTO> convertFromAccountListToAccountDtoList(List<Account> accounts);

    public Account convertFromAccountSimpleDtoToAccount(AccountSimpleDTO accountSimpleDTO);

    public AccountSimpleDTO convertFromAccountToAccountSimpleDto(Account account);

    public List<Account> convertFromAccountSimpleDtoListToAccountList(List<AccountSimpleDTO> accountSimpleDtoList);

    public List<AccountSimpleDTO> convertFromAccountListToAccountSimpleDtoList(List<Account> accounts);

}
