package com.restaurant.spring.service.impl;

import com.restaurant.spring.dto.NotificationDto;
import com.restaurant.spring.dto.security.AccountDto;
import com.restaurant.spring.mapper.NotificationMapper;
import com.restaurant.spring.mapper.security.AccountMapper;
import com.restaurant.spring.model.Notification;
import com.restaurant.spring.model.security.Account;
import com.restaurant.spring.repo.NotificationRepo;
import com.restaurant.spring.repo.security.AccountRepo;
import com.restaurant.spring.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepo notificationRepo;
    private AccountRepo accountRepo;
    private NotificationMapper notificationMapper;
    private AccountMapper accountMapper;

    @Autowired
    public NotificationServiceImpl(NotificationRepo notificationRepo, AccountRepo accountRepo, NotificationMapper notificationMapper, AccountMapper accountMapper) {
        this.notificationRepo = notificationRepo;
        this.accountRepo = accountRepo;
        this.notificationMapper = notificationMapper;
        this.accountMapper = accountMapper;
    }

    @Override
    public NotificationDto createNotification(NotificationDto notificationDto) {

        if(Objects.nonNull(notificationDto.getId())){
            throw new RuntimeException("id.must_be.null");
        }

        Notification notification = notificationMapper.toNotification(notificationDto);

        if(notificationDto.getAccount() == null){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            AccountDto accountDto = (AccountDto) auth.getPrincipal();
            Account account = accountMapper.toAccount(accountDto);
            notification.setAccount(account);
        }else{
            Account account = accountMapper.toAccount(notificationDto.getAccount());
            notification.setAccount(account);
        }
        notification.setCreatedDate(LocalDateTime.now());
        Notification saved = notificationRepo.save(notification);
        return notificationMapper.toNotificationDto(saved);
    }

    @Override
    public List<NotificationDto> getNotificationsByUser(String username) {
        Account account = accountRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("account.username.notExists"));
        List<Notification> notifications = notificationRepo.findByAccountOrderByCreatedDateDesc(account);
        if(notifications.isEmpty()){
            throw new RuntimeException("notifications.not.found");
        }
        return notificationMapper.toNotificationDtoList(notifications);
    }

    @Override
    public List<NotificationDto> getUnreadNotifications(String username) {
        Account account = accountRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("account.username.notExists"));
        List<Notification> notifications = notificationRepo.findByAccountAndReadFalseOrderByCreatedDateDesc(account);
        if(notifications.isEmpty()){
            throw new RuntimeException("notifications.not.found");
        }
        return notificationMapper.toNotificationDtoList(notifications);
    }

    @Override
    public NotificationDto markAsRead(Long notificationId) {

        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("notification.not.found"));
        notification.setRead(true);
        notification.setUpdatedDate(LocalDateTime.now());
        Notification updated = notificationRepo.save(notification);
        return notificationMapper.toNotificationDto(updated);
    }

    @Override
    public void handleNotification(Account user, Account admin, String subject,String type){
        Notification notification = new Notification();
        notification.setType(type);
        switch (type){
            case "NEW_MESSAGE":
                notification.setAccount(admin);
                notification.setMessage("User " + user.getUsername() + " sent a new message: \"" + subject + "\"");
                break;
            case "UPDATE_MESSAGE":
//                Account admin = accountRepo.findFirstByRoles_RoleNameIgnoreCase("ADMIN")
//                        .orElseThrow(() -> new RuntimeException("admin.not.found"));
                notification.setAccount(admin);
                notification.setMessage("User " + user.getUsername() + " updated their message: \"" + subject + "\"");
                break;
            case "ADMIN_REPLY":
                notification.setAccount(user);
                notification.setMessage("Admin replied to your message: \"" + subject + "\"");
                break;
            default:
                throw new IllegalArgumentException("Unknown notification type: " + type);
        }

        notification.setRead(false);
        notificationRepo.save(notification);
    }
}
