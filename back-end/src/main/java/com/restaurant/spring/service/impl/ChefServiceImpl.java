package com.restaurant.spring.service.impl;


import com.restaurant.spring.dto.ChefDto;
import com.restaurant.spring.mapper.ChefMapper;
import com.restaurant.spring.model.Chef;
import com.restaurant.spring.repo.ChefRepo;
import com.restaurant.spring.service.ChefService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChefServiceImpl implements ChefService {

    private ChefRepo  chefRepo;
    private ChefMapper chefMapper;

    public ChefServiceImpl(ChefRepo chefRepo, ChefMapper chefMapper) {
        this.chefRepo = chefRepo;
        this.chefMapper = chefMapper;
    }

    @Override
    @Cacheable(value = "chefs", key = "'allChefs'")
    public List<ChefDto> getAllChefs() {
        List<Chef> chefs = chefRepo.findAllByOrderByIdAsc();
        if (chefs.isEmpty()) {
            throw new RuntimeException("chefs.not.found");
        }
        return chefMapper.toChefDtoList(chefs);
    }
}
