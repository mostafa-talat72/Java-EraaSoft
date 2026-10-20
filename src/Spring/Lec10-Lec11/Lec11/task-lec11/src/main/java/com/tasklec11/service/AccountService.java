package com.tasklec11.service;

import com.tasklec11.dto.AccountDTO;

public interface AccountService{

    public AccountDTO getByEmail(String email);
}
