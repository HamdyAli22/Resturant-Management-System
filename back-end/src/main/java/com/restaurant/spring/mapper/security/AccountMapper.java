package com.restaurant.spring.mapper.security;

import com.restaurant.spring.dto.security.AccountDto;
import com.restaurant.spring.model.security.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring",uses = {RoleMapper.class, AccountDetailsMapper.class})
public interface AccountMapper {

    @Mapping(target = "enabled", source = "enabled")
    Account toAccount(AccountDto accountDto);

    @Mapping(target = "enabled", source = "enabled")
    AccountDto toAccountDto(Account account);

    List<Account> toAccountList(List<AccountDto> accountDto);

    List<AccountDto> toAccountDtoList(List<Account> accounts);

}
