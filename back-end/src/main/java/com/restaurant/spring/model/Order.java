package com.restaurant.spring.model;

import com.restaurant.spring.model.security.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private double totalPrice;

    @Column(nullable = false)
    private double totalNumber;


    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    List<Product> products;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Account account;

    @Column(name = "order_date", nullable = false, updatable = false)
    private java.time.LocalDateTime orderDate;

    @PrePersist
    public void prePersist() {
        this.orderDate = java.time.LocalDateTime.now();
    }
}
