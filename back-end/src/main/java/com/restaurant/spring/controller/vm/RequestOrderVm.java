package com.restaurant.spring.controller.vm;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RequestOrderVm {

    private double totalPrice;

    private double totalNumber;

    List<Long> productsIds;
}
