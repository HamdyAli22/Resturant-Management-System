package com.restaurant.spring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="chefs")
public class Chef extends BaseEntity{

    @Column(nullable = false, length = 120, unique = true)
    private String name;

    private String spec;

    private String logoPath;

    private String faceLink;

    private String tweLink;

    private String instaLink;

}
