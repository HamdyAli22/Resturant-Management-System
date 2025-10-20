package com.restaurant.spring.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChefDto implements Serializable {

    private Long id;

    @NotEmpty(message = "chef.name.required")
    @Size(min = 7, max = 100, message = "chef.size.name")
    private String name;

    @NotEmpty(message = "chef.spec.required")
    private String spec;

    @NotEmpty(message = "chef.logoPath.required")
    private String logoPath;

    @NotEmpty(message = "chef.faceLink.required")
    private String faceLink;

    @NotEmpty(message = "chef.tweLink.required")
    private String tweLink;

    @NotEmpty(message = "chef.instaLink.required")
    private String instaLink;
}
