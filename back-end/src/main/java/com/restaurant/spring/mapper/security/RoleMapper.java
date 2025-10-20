package com.restaurant.spring.mapper.security;

import com.restaurant.spring.dto.security.RoleDto;
import com.restaurant.spring.model.security.Role;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {


    Role toRole(RoleDto roleDto);

    //@Mapping(target = "account", ignore = true)
    RoleDto toRoleDto(Role role);

    List<Role> toRoleList (List<RoleDto> roleDto);

    List<RoleDto> toRoleDtoList (List<Role> roles);
}
