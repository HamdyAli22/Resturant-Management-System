package com.restaurant.spring.repo;

import com.restaurant.spring.model.Notification;
import com.restaurant.spring.model.security.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification,Long> {

    List<Notification> findByAccountOrderByCreatedDateDesc(Account account);

    List<Notification> findByAccountAndReadFalseOrderByCreatedDateDesc(Account account);
}
