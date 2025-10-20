package com.restaurant.spring.controller.vm;

import com.restaurant.spring.dto.ProductDto;
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
public class ProductResponseVm implements Serializable {
private List<ProductDto> products;
private Long totalProducts;
}
