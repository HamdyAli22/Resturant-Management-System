package com.restaurant.spring.mapper;

import com.restaurant.spring.dto.NotificationDto;
import com.restaurant.spring.mapper.security.AccountMapper;
import com.restaurant.spring.model.Notification;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AccountMapper.class})
public interface NotificationMapper {

    NotificationDto toNotificationDto(Notification notification);
    Notification toNotification(NotificationDto notificationDto);
    List<NotificationDto> toNotificationDtoList(List<Notification> notifications);
    List<Notification> toNotificationList(List<NotificationDto> notificationDto);

}
