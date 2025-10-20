package com.restaurant.spring.mapper;

import com.restaurant.spring.dto.ChefDto;
import com.restaurant.spring.model.Chef;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChefMapper {

    Chef toChef (ChefDto chefDto);

    ChefDto toChefDto (Chef chef);

    List<Chef> toChefList(List<ChefDto> chefDtos );

    List<ChefDto> toChefDtoList(List<Chef> chefs);
}
