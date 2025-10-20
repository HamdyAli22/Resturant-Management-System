
package com.restaurant.spring.service.impl.security;

import com.restaurant.spring.controller.vm.AuthRequestVm;
import com.restaurant.spring.controller.vm.AuthResponseVm;
import com.restaurant.spring.dto.security.AccountDto;
import com.restaurant.spring.dto.security.RoleDto;
import com.restaurant.spring.service.security.AccountService;
import com.restaurant.spring.service.security.AuthService;
import com.restaurant.spring.config.security.TokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private AccountService accountService;
    private TokenHandler tokenHandler;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(AccountService accountService, TokenHandler tokenHandler, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.tokenHandler = tokenHandler;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public AuthResponseVm login(AuthRequestVm authRequestVm) {
        AccountDto accountDto = accountService.getByUserName(authRequestVm.getUsername());

        if (!accountDto.isEnabled()) {
            throw new RuntimeException("account.disabled");
        }

        if(!passwordEncoder.matches(authRequestVm.getPassword(), accountDto.getPassword())) {
            throw new RuntimeException("auth.invalid.password");
        }

        AuthResponseVm authResponseVm = new AuthResponseVm();
        authResponseVm.setUsername(accountDto.getUsername());
        authResponseVm.setToken(tokenHandler.createToken(accountDto));
        authResponseVm.setUserRoles(getAccountRoles(accountDto));

        return authResponseVm;
    }

    @Override
    public AuthResponseVm signup(AccountDto accountDto) {

        AccountDto savedAccountDto =  accountService.createAccount(accountDto);
        if(Objects.isNull(savedAccountDto)){
            throw new RuntimeException("account.not.created");
        }

        AuthResponseVm authResponseVm = new AuthResponseVm();
        authResponseVm.setUsername(savedAccountDto.getUsername());
        authResponseVm.setToken(tokenHandler.createToken(savedAccountDto));
        authResponseVm.setUserRoles(getAccountRoles(savedAccountDto));

        return authResponseVm;
    }

    private List<String> getAccountRoles(AccountDto accountDto) {
        return accountDto.getRoles().stream().map(RoleDto::getRoleName).collect(Collectors.toList());
    }

}
