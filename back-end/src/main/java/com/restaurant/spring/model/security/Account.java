package com.restaurant.spring.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restaurant.spring.model.ContactInfo;
import com.restaurant.spring.model.Notification;
import com.restaurant.spring.model.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private boolean enabled = true;

    @OneToOne(mappedBy = "account",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AccountDetails accountDetails;

    @ManyToMany
    @JoinTable(
            name = "account_roles",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Order> orders;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private List<ContactInfo> contacts;

    @OneToMany(mappedBy = "account")
    private List<Notification> notifications;
}
