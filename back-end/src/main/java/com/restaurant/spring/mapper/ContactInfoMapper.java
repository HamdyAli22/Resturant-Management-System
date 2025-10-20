package com.restaurant.spring.mapper;

import com.restaurant.spring.dto.ContactInfoDto;
import com.restaurant.spring.mapper.security.AccountMapper;
import com.restaurant.spring.model.ContactInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AccountMapper.class})
public interface ContactInfoMapper {


    ContactInfoDto toContactInfoDto(ContactInfo contactInfo);

    @Mapping(target = "account.contacts", ignore = true)
    ContactInfo toContactInfo(ContactInfoDto contactInfoDto);

    List<ContactInfoDto> toContactInfoDtoList(List<ContactInfo> contactInfos);

    List<ContactInfo> toContactInfoList(List<ContactInfoDto> contactInfoDtos);
}
