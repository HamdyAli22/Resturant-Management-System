package com.restaurant.spring.controller;

import com.restaurant.spring.dto.CategoryDto;
import com.restaurant.spring.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin("http://localhost:4200")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
   // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/save-category")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> saveCategory(@RequestBody @Valid CategoryDto categoryDto) {
        CategoryDto savedCategory = categoryService.saveCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @PostMapping("/save-category/bulk")
  //  @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CategoryDto>> saveCategories(@RequestBody @Valid List<CategoryDto> categoryDtos) {
        List<CategoryDto> savedCategories = categoryService.saveCategories(categoryDtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategories);
    }

    @PutMapping("/update-category")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody @Valid CategoryDto categoryDto) {
        CategoryDto updatedCategory = categoryService.updateCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCategory);
    }

    @PutMapping("/update-category/bulk")
    //  @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CategoryDto>> updateCategories(@RequestBody @Valid List<CategoryDto> categoryDtos) {
        List<CategoryDto> updatedCategories = categoryService.updateCategories(categoryDtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCategories);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCategoryByID(@RequestParam Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/bulk")
    public ResponseEntity<Void> deleteCategories(@RequestParam List<Long> ids) {
        categoryService.deleteCategories(ids);
        return ResponseEntity.noContent().build();
    }

}
