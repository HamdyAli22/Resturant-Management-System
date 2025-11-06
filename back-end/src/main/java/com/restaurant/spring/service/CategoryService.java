package com.restaurant.spring.service;

import com.restaurant.spring.controller.vm.CategoryResponseVm;
import com.restaurant.spring.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryResponseVm getAllCategoriesWithPagination(int page, int size);
    List<CategoryDto> getAllCategories();
    CategoryDto saveCategory(CategoryDto categoryDto);
    List<CategoryDto> saveCategories(List<CategoryDto> categoryDtos);
    CategoryDto  updateCategory(CategoryDto categoryDto);
    List<CategoryDto> updateCategories(List<CategoryDto> categoryDtos);
    void deleteCategoryById(Long id);
    void deleteCategories(List<Long> ids);
    CategoryResponseVm searchCategories(String keyword, int page, int size);
}
