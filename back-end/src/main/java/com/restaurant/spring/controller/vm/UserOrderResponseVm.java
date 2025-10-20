package com.restaurant.spring.controller.vm;

import com.restaurant.spring.dto.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserOrderResponseVm implements Serializable {
    private List<OrderDto>  orders;

    private Integer size;

    private double price;

    private Long totalOrders;

    public UserOrderResponseVm(List<OrderDto> orders, Integer size,double price) {
        this.price = price;
        this.size = size;
        this.orders = orders;
    }

}
