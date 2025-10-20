package com.restaurant.spring.mapper.security;

import com.restaurant.spring.dto.security.AccountDetailsDto;
import com.restaurant.spring.model.security.AccountDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountDetailsMapper {

    @Mapping(target = "account", ignore = true)
    AccountDetails toAccountDetails (AccountDetailsDto  accountDetailsDto);

    @Mapping(target = "account", ignore = true)
    AccountDetailsDto toAccountDetailsDto (AccountDetails accountDetails);

    List<AccountDetails> toAccountDetailsList (List<AccountDetailsDto>  accountDetailsDto);

    List<AccountDetailsDto> toAccountDetailsDtoList (List<AccountDetails> accountDetails);
}
