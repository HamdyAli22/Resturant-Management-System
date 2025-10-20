package com.restaurant.spring.model;

import com.restaurant.spring.model.security.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Notification extends BaseEntity{

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private boolean read = false;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
