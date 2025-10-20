package com.restaurant.spring.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "product_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetails extends BaseEntity{
    @Column(length = 1000)
    private String ingredients;

    private Boolean stockAvailability;

    @Column(length = 1000)
    private String specialInstructions;

    private LocalDate expiryDate;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;
}
