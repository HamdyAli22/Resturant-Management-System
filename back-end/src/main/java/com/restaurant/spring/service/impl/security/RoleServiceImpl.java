package com.restaurant.spring.service.impl.security;

import com.restaurant.spring.dto.security.RoleDto;
import com.restaurant.spring.mapper.security.RoleMapper;
import com.restaurant.spring.repo.security.RoleRepo;
import com.restaurant.spring.service.security.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepo roleRepo;
    private RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepo roleRepo, RoleMapper roleMapper) {
        this.roleRepo = roleRepo;
        this.roleMapper = roleMapper;
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        return null;
    }
}
