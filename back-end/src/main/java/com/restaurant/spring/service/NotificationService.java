package com.restaurant.spring.service;

import com.restaurant.spring.dto.NotificationDto;
import com.restaurant.spring.dto.security.AccountDto;
import com.restaurant.spring.model.security.Account;

import java.util.List;


public interface NotificationService {
    NotificationDto createNotification(NotificationDto notificationDto);

    List<NotificationDto> getNotificationsByUser(String username);

    List<NotificationDto> getUnreadNotifications(String username);

    NotificationDto markAsRead(Long notificationId);

    void handleNotification(Account user, Account admin, String message,String type);

    void deleteNotificationById(Long id);

    void markAllAsRead(String username);

}
