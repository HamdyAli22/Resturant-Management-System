package com.restaurant.spring.mapper;

import com.restaurant.spring.dto.CategoryDto;
import com.restaurant.spring.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses =  {ProductMapper.class})
public interface CategoryMapper {

  CategoryDto toCategoryDto(Category category);

  @Mapping(target = "products", ignore = true)
  Category toCategory(CategoryDto categoryDto);

  List<CategoryDto> toCategoryDtoList(List<Category> categories);

  List<Category> toCategoryList(List<CategoryDto> categoriesDto);

}
