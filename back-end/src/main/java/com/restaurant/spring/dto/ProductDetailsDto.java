package com.restaurant.spring.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDetailsDto implements Serializable {
    private Long id;

    @NotEmpty(message = "product.details.ingredients.required")
    @Size(min = 10, max = 300, message = "product.details.ingredients.size")
    private String ingredients;

    private Boolean stockAvailability;

    @Size(min = 10, max = 300, message = "product.details.specialInstructions.size")
    private String specialInstructions;

    @NotNull(message = "product.details.expiryDate.required")
    @FutureOrPresent(message = "product.details.expiryDate.future")
    private LocalDate expiryDate;

    private Long productId;
}
