package com.restaurant.spring.service.impl;

import com.restaurant.spring.dto.CategoryDto;
import com.restaurant.spring.mapper.CategoryMapper;
import com.restaurant.spring.model.Category;
import com.restaurant.spring.repo.CategoryRepo;
import com.restaurant.spring.service.CategoryService;
import com.restaurant.spring.service.bundlemessage.BundleMessageService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl  implements CategoryService {

    private CategoryRepo  categoryRepo;
    private CategoryMapper  categoryMapper;
    private BundleMessageService bundleMessageService;

    public CategoryServiceImpl(CategoryRepo categoryRepo, CategoryMapper categoryMapper, BundleMessageService bundleMessageService) {
        this.categoryRepo = categoryRepo;
        this.categoryMapper = categoryMapper;
        this.bundleMessageService = bundleMessageService;
    }

    @Override
    @Cacheable(value = "categories", key = "'allCategories'")
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepo.findAllByOrderByNameAsc();
        if (categories.isEmpty()) {
            throw new RuntimeException("category.not.found");
        }
        return categoryMapper.toCategoryDtoList(categories);
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryDto saveCategory(CategoryDto categoryDto) {

        if(Objects.nonNull(categoryDto.getId())){
            throw new RuntimeException("id.must_be.null");
        }

        if(categoryRepo.existsByName(categoryDto.getName())){
            throw new RuntimeException("category.name.exist");
        }

       Category category = categoryMapper.toCategory(categoryDto);

        return categoryMapper.toCategoryDto(categoryRepo.save(category));
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public List<CategoryDto> saveCategories(List<CategoryDto> categoryDtos) {
        List<Category> categories = categoryDtos.stream()
                .peek(dto -> {
                    if (Objects.nonNull(dto.getId())) {
                        throw new RuntimeException(
                                bundleMessageService.getMessage("id.must_be.null")
                        );
                    }
                    if (categoryRepo.existsByName(dto.getName())) {
                        throw new RuntimeException(
                                bundleMessageService.getMessage("category.name.exist")
                        );
                    }
                })
                .map(categoryMapper::toCategory)
                .toList();

        return categoryMapper.toCategoryDtoList(categoryRepo.saveAll(categories));
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    @CachePut(value = "categories", key = "#categoryDto.id")
    public CategoryDto updateCategory(CategoryDto categoryDto) {

        if(categoryDto.getId() == null){
            throw new RuntimeException("id.must_be.not_null");
        }

        categoryRepo.findById(categoryDto.getId())
                .orElseThrow(() -> new RuntimeException("category.not.found"));

        if (categoryRepo.existsByNameAndIdNot(categoryDto.getName(), categoryDto.getId())) {
            throw new RuntimeException("category.name.exist");
        }

        Category category = categoryMapper.toCategory(categoryDto);

        return categoryMapper.toCategoryDto(categoryRepo.save(category));
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public List<CategoryDto> updateCategories(List<CategoryDto> categoryDtos) {
        List<Category> categories = categoryDtos.stream()
                .peek( dto -> {
                    if (dto.getId() == null) {
                        throw new RuntimeException("category.id.required");
                    }

                    categoryRepo.findById(dto.getId())
                                    .orElseThrow(() -> new RuntimeException("category.not.found"));

                    if (categoryRepo.existsByNameAndIdNot(dto.getName(), dto.getId())) {
                                throw new RuntimeException("category.name.exist");
                            }

                 }).map(categoryMapper::toCategory).toList();
        return categoryMapper.toCategoryDtoList(categoryRepo.saveAll(categories));
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategoryById(Long id) {
        categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("category.not.found"));
        categoryRepo.deleteById(id);
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategories(List<Long> ids) {
        List<Category> categories = categoryRepo.findAllById(ids);
        if (categories.isEmpty()) {
            throw new RuntimeException("category.not.found");
        }
        categoryRepo.deleteAll(categories);
    }

}
