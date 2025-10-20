package com.restaurant.spring.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto implements Serializable {

    private Long id;

    @NotEmpty(message = "product.name.required")
    @Size(min = 7, max = 50, message = "product.name.size")
    private String name;

    @NotNull(message = "product.price.required")
    @DecimalMin(value = "0.01", message = "product.price.min")
    private Double price;

    @NotEmpty(message = "product.desc.required")
    @Size(min = 10, max = 300, message = "product.desc.size")
    private String description;

    @NotEmpty(message = "product.image.required")
    private String image;

    @NotNull(message = "product.category.required")
    private Long categoryId;

    @Valid
    private ProductDetailsDto productDetailsDto;

}
