package com.restaurant.spring.service.security;

import com.restaurant.spring.controller.vm.ChangePassReqVM;
import com.restaurant.spring.dto.security.AccountDetailsDto;
import com.restaurant.spring.dto.security.AccountDto;
import com.restaurant.spring.helper.BundleMessage;

import java.util.List;

public interface AccountService {
    AccountDto getByUserName(String username);
    AccountDto createAccount(AccountDto accountDto);
    String changePassword(ChangePassReqVM request);
    String resetPassword(ChangePassReqVM request);
    List<AccountDto> getAllAccounts();
    String toggleAccountStatus(Long id);
    AccountDto updateAccountDetails(String username, AccountDetailsDto accountDetailsDto);
}
