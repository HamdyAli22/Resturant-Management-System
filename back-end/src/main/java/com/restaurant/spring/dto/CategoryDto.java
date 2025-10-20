package com.restaurant.spring.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.restaurant.spring.model.Product;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto implements Serializable {

    private Long id;

    @NotEmpty(message = "category.name.required")
    private String name;

    @NotEmpty(message = "category.logo.required")
    private String logo;

    @NotEmpty(message = "category.flag.required")
    private String flag;

}
