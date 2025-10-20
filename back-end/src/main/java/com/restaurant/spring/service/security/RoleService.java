package com.restaurant.spring.service.security;

import com.restaurant.spring.dto.security.RoleDto;

public interface RoleService {
    RoleDto createRole(RoleDto roleDto);
}
