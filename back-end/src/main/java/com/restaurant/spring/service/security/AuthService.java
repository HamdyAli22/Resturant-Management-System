package com.restaurant.spring.service.security;

import com.restaurant.spring.controller.vm.AuthRequestVm;
import com.restaurant.spring.controller.vm.AuthResponseVm;
import com.restaurant.spring.dto.security.AccountDto;

public interface AuthService {

    AuthResponseVm login(AuthRequestVm authRequestVm);
    AuthResponseVm signup(AccountDto accountDto);
}
