package com.restaurant.spring.service.impl;

import com.restaurant.spring.controller.vm.ContactInfoResponseVm;
import com.restaurant.spring.dto.ContactInfoDto;
import com.restaurant.spring.dto.security.AccountDto;
import com.restaurant.spring.mapper.ContactInfoMapper;
import com.restaurant.spring.mapper.security.AccountMapper;
import com.restaurant.spring.model.ContactInfo;
import com.restaurant.spring.model.Notification;
import com.restaurant.spring.model.Product;
import com.restaurant.spring.model.security.Account;
import com.restaurant.spring.repo.ContactInfoRepo;
import com.restaurant.spring.repo.NotificationRepo;
import com.restaurant.spring.repo.security.AccountRepo;
import com.restaurant.spring.service.ContactInfoService;
import com.restaurant.spring.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ContactInfoServiceImpl implements ContactInfoService {

    private ContactInfoRepo contactInfoRepo;
    private ContactInfoMapper contactInfoMapper;
    private AccountMapper accountMapper;
    private AccountRepo accountRepo;
    private NotificationRepo notificationRepo;
    private NotificationService notificationService;
    private String NotificationType;

    @Autowired
    public ContactInfoServiceImpl(ContactInfoRepo contactInfoRepo,
                                  ContactInfoMapper contactInfoMapper,
                                  AccountMapper accountMapper,
                                  AccountRepo accountRepo,
                                  NotificationRepo notificationRepo,
                                  NotificationService notificationService) {
        this.contactInfoRepo = contactInfoRepo;
        this.contactInfoMapper = contactInfoMapper;
        this.accountMapper = accountMapper;
        this.accountRepo = accountRepo;
        this.notificationRepo = notificationRepo;
        this.notificationService = notificationService;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "contacts", allEntries = true),
            @CacheEvict(value = "contactsByUsername", allEntries = true),
            @CacheEvict(value = "searchContacts", allEntries = true)
    })
    public ContactInfoDto createContactInfo(ContactInfoDto contactInfoDto) {

        if (Objects.nonNull(contactInfoDto.getId())) {
            throw new RuntimeException("id.must_be.null");
        }

        ContactInfo contactInfo = contactInfoMapper.toContactInfo(contactInfoDto);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDto accountDto = (AccountDto)authentication.getPrincipal();

        if (accountDto.getAccountDetails() == null) {
            throw new RuntimeException("account.details.not.found");
        }

        Account account = accountMapper.toAccount(accountDto);

        contactInfo.setAccount(account);

        ContactInfo savedContact = contactInfoRepo.save(contactInfo);

        Account admin = accountRepo.findFirstByRoles_RoleNameIgnoreCase("ADMIN")
                .orElseThrow(() -> new RuntimeException("admin.not.found"));

        notificationService.handleNotification(account,admin,contactInfo.getSubject(), "NEW_MESSAGE");

        return contactInfoMapper.toContactInfoDto(savedContact);
    }

    @Override
    @Cacheable(
            value = "contacts",
            key = "'page_' + #page + '_size_' + #size"
    )
    public ContactInfoResponseVm getAllContacts(int page, int size) {
        Pageable pageable = getPageable(page, size);
        Page<ContactInfo> contacts = contactInfoRepo.findAllByOrderByCreatedDateDesc(pageable);
       if(contacts.isEmpty()){
           throw new RuntimeException("contacts.not.found");
       }
       List<ContactInfoDto> contactInfoDtos = contactInfoMapper.toContactInfoDtoList(contacts.getContent());
       return new ContactInfoResponseVm(contactInfoDtos,contacts.getTotalElements());
    }

    @Override
    @Cacheable(
            value = "contactsByUsername",
            key = "'username_' + #username + '_page_' + #page + '_size_' + #size"
    )
    public ContactInfoResponseVm getContactsByUsername(String username,int page, int size) {
        Pageable pageable = getPageable(page, size);
        Account account = accountRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("account.username.notExists"));

        Page<ContactInfo> contacts = contactInfoRepo.findByAccountOrderByCreatedDateDesc(account,pageable);

        List<ContactInfoDto> contactInfoDtos = contactInfoMapper.toContactInfoDtoList(contacts.getContent());
        return new ContactInfoResponseVm(contactInfoDtos,contacts.getTotalElements());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "contacts", allEntries = true),
            @CacheEvict(value = "contactsByUsername", allEntries = true),
            @CacheEvict(value = "searchContacts", allEntries = true)
    })
    public ContactInfoDto updateMessage(ContactInfoDto contactInfoDto) {

        Account user;
        ContactInfo existing = contactInfoRepo.findById(contactInfoDto.getId())
                .orElseThrow(() -> new RuntimeException("contact.not.found"));

        ContactInfo updatedEntity = contactInfoMapper.toContactInfo(contactInfoDto);
        updatedEntity.setId(existing.getId());
        updatedEntity.setCreatedDate(existing.getCreatedDate());
        updatedEntity.setUpdatedDate(LocalDateTime.now());
        updatedEntity.setAccount(existing.getAccount());
        ContactInfo saved = contactInfoRepo.save(updatedEntity);

        //handleNotification
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDto currentUserDto = (AccountDto) authentication.getPrincipal();
        Account currentAccount = accountMapper.toAccount(currentUserDto);

        boolean isAdmin = currentAccount.getRoles().stream()
                .anyMatch(r -> r.getRoleName().equalsIgnoreCase("ADMIN"));

        Account admin = accountRepo.findFirstByRoles_RoleNameIgnoreCase("ADMIN")
                        .orElseThrow(() -> new RuntimeException("admin.not.found"));

        if(isAdmin){
            NotificationType = "ADMIN_REPLY";
            user = existing.getAccount();
        }else{
            NotificationType = "UPDATE_MESSAGE";
            user = currentAccount;
        }

        notificationService.handleNotification(user,admin,existing.getSubject(),NotificationType);
        //End handleNotification

        return contactInfoMapper.toContactInfoDto(saved);
    }

    @Override
    @Cacheable(
            value = "searchContacts",
            key = "'keyword_' + #keyword + '_page_' + #page + '_size_' + #size"
    )
    public ContactInfoResponseVm searchContacts(String keyword, int page, int size) {

        Pageable pageable = getPageable(page, size);

        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllContacts(page, size);
        }

        List<ContactInfo> contacts = contactInfoRepo.searchContacts(keyword.trim());

        if (contacts.isEmpty()) {
            throw new RuntimeException("contacts.not.found");
        }

        long total = contacts.size();
        int totalPages = (int) Math.ceil((double) total / size);
        if(page > totalPages){
            page = 1;
        }

        int start = (page - 1) * size;
        int end = Math.min(start + size, contacts.size());
        if (start >= contacts.size()) {
            throw new RuntimeException("contacts.not.found");
        }

        List<ContactInfo> paginatedList = contacts.subList(start, end);

        List<ContactInfoDto> contactInfoDtos = contactInfoMapper.toContactInfoDtoList(paginatedList);

        return new ContactInfoResponseVm(contactInfoDtos,  (long) contacts.size());
    }

    private static Pageable getPageable(int page, int size) {
        try {
            if (page < 1) {
                throw new RuntimeException("error.min.one.page");
            }
            return PageRequest.of(page - 1, size);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
