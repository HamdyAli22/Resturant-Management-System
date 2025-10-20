package com.restaurant.spring.service;

import com.restaurant.spring.dto.NotificationDto;
import com.restaurant.spring.dto.security.AccountDto;

import java.util.List;


public interface NotificationService {
    NotificationDto createNotification(NotificationDto notificationDto);

    List<NotificationDto> getNotificationsByUser(String username);

    List<NotificationDto> getUnreadNotifications(String username);

    NotificationDto markAsRead(Long notificationId);
}
