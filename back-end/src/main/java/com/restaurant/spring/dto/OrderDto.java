package com.restaurant.spring.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.restaurant.spring.dto.security.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto implements Serializable {

    private Long id;

    private String code;

    private double totalPrice;

    private double totalNumber;

    private LocalDateTime orderDate;

    List<ProductDto> products;

    private AccountDto account;
}
