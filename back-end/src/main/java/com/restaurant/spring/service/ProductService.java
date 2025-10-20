package com.restaurant.spring.service;

import com.restaurant.spring.controller.vm.ProductResponseVm;
import com.restaurant.spring.dto.ProductDto;
import com.restaurant.spring.dto.ProductDetailsDto;

import java.util.List;

public interface ProductService {
    ProductResponseVm getProductsByCategoryId(Long categoryId,int page, int size);
    ProductResponseVm getAllProducts(int page, int size);
    ProductDto saveProduct(ProductDto productDto);
    List<ProductDto> saveProducts(List<ProductDto> productDtos);
    ProductDto updateProduct(ProductDto productDto);
    List<ProductDto> updateProducts(List<ProductDto> productDtos);
    void deleteProductById(Long id);
    void deleteProducts(List<Long> ids);
    ProductResponseVm searchProducts(String key,int page, int size);
    List<ProductDto> getProductByIds(List<Long> ids);
    ProductDto addDetails(Long productId, ProductDetailsDto detailsDto);
}
