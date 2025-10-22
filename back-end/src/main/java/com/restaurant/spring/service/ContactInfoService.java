package com.restaurant.spring.service;

import com.restaurant.spring.controller.vm.ContactInfoResponseVm;
import com.restaurant.spring.dto.ContactInfoDto;
import com.restaurant.spring.model.ContactInfo;

import java.util.List;

public interface ContactInfoService {
    ContactInfoDto createContactInfo(ContactInfoDto contactInfoDto);
    ContactInfoResponseVm getAllContacts(int page, int size);
    ContactInfoResponseVm getContactsByUsername(String username,int page, int size);
    ContactInfoDto updateMessage(ContactInfoDto contactInfoDto);
    ContactInfoResponseVm searchContacts(String keyword, int page, int size);
}
