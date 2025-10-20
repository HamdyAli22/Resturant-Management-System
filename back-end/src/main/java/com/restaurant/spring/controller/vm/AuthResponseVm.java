package com.restaurant.spring.controller.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseVm {

    private String username;
    private String token;
    @JsonProperty("roles")
    private List<String> userRoles;
}
