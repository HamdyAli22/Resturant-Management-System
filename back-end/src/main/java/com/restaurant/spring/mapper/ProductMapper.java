package com.restaurant.spring.mapper;

import com.restaurant.spring.dto.ProductDto;
import com.restaurant.spring.model.Category;
import com.restaurant.spring.model.Product;
import com.restaurant.spring.model.ProductDetails;
import com.restaurant.spring.dto.ProductDetailsDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "categoryId" , target = "category")
    Product toProduct(ProductDto productDto);

    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toProductDto(Product product);

    List<Product> toProductList(List<ProductDto> productDtoList);

    List<ProductDto> toProductDtoList(List<Product> products);

    default Category map(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    ProductDetails toProductDetails(ProductDetailsDto dto);

    ProductDetailsDto toProductDetailsDto(ProductDetails entity);

    @Mapping(source = "categoryId" , target = "category")
    void updateProductFromDto(ProductDto dto, @MappingTarget Product entity);

    @Mapping(target = "id", ignore = true)
    void updateProductDetailsFromDto(ProductDetailsDto dto, @MappingTarget ProductDetails entity);

    @AfterMapping
    default void mapDetails(Product product, @MappingTarget ProductDto productDto) {
        if (product.getProductDetails() != null) {
            productDto.setProductDetailsDto(toProductDetailsDto(product.getProductDetails()));
        }
    }

}
