package com.tasklec11.mapper;

import com.tasklec11.dto.AccountDTO;
import com.tasklec11.model.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    public Account convertFromAccountDtoToAccount(AccountDTO accountDTO);

    public AccountDTO convertFromAccountToAccountDto(Account account);

    public List<Account> convertFromAccountDtoListToAccountList(List<AccountDTO> accountDTOList);

    public List<AccountDTO> convertFromAccountListToAccountDtoList(List<Account> accounts);
    
}
