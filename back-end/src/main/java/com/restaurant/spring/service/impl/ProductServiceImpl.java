package com.restaurant.spring.service.impl;

import com.restaurant.spring.dto.ProductDto;
import com.restaurant.spring.mapper.ProductMapper;
import com.restaurant.spring.model.Category;
import com.restaurant.spring.model.Product;
import com.restaurant.spring.model.ProductDetails;
import com.restaurant.spring.dto.ProductDetailsDto;
import com.restaurant.spring.repo.CategoryRepo;
import com.restaurant.spring.repo.ProductRepo;
import com.restaurant.spring.service.NotificationService;
import com.restaurant.spring.service.ProductService;
import com.restaurant.spring.service.bundlemessage.BundleMessageService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import com.restaurant.spring.controller.vm.ProductResponseVm;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepo productRepo;
    private ProductMapper productMapper;
    private CategoryRepo categoryRepo;
    private BundleMessageService bundleMessageService;
    private NotificationService notificationService;

    public ProductServiceImpl(ProductRepo productRepo,
                              ProductMapper productMapper,
                              CategoryRepo categoryRepo,
                              BundleMessageService bundleMessageService,
                              NotificationService notificationService) {
        this.productRepo = productRepo;
        this.productMapper = productMapper;
        this.categoryRepo = categoryRepo;
        this.bundleMessageService = bundleMessageService;
        this.notificationService = notificationService;
    }

    @Override
    @Cacheable(
            value = "productsByCategory",
            key = "'category_' + #categoryId + '_page_' + #page + '_size_' + #size"
    )
    public ProductResponseVm getProductsByCategoryId(Long categoryId,int  page, int size) {
        Pageable pageable = getPageable(page, size);
        Page<Product> products = productRepo.findByCategoryId(categoryId,pageable);
        if(products.isEmpty()){
            throw new RuntimeException("product.category.not.found");
        }
        return new ProductResponseVm(productMapper.toProductDtoList(products.getContent()),products.getTotalElements());

    }

    @Override
    @Cacheable(value = "products", key = "'allProducts_page_' + #page + '_size_' + #size")
    public ProductResponseVm getAllProducts(int page,int size) {
        Pageable pageable = getPageable(page, size);
        Page<Product> products = productRepo.findAllByOrderByIdAsc(pageable);
        if(products.isEmpty()){
            throw new RuntimeException("product.not.found");
        }
        return new ProductResponseVm(productMapper.toProductDtoList(products.getContent()),products.getTotalElements());
    }

    @Override
    @Cacheable(value = "searchProducts", key = "'key_' + #key + '_page_' + #page + '_size_' + #size")
    public ProductResponseVm searchProducts(String key,int page,int size) {
        Pageable pageable = getPageable(page, size);
        List<Product> products = productRepo.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(key, key);

        if (products.isEmpty()) {
            throw new RuntimeException("product.not.found");
        }

        long total = products.size();
        int totalPages = (int) Math.ceil((double) total / size);
        if(page > totalPages){
            page = 1;
        }

        int start = (page - 1) * size;
        int end = Math.min(start + size, products.size());
        if (start >= products.size()) {
            throw new RuntimeException("product.not.found");
        }
        List<Product> paginatedList = products.subList(start, end);
        return new ProductResponseVm(
                productMapper.toProductDtoList(paginatedList),
                (long) products.size()
        );
    }

    @Override
    public List<ProductDto> getProductByIds(List<Long> ids) {
        return productMapper.toProductDtoList(productRepo.findAllById(ids));
    }

    private static Pageable getPageable(int page, int size) {
        try {
            if (page < 1) {
                throw new RuntimeException("error.min.one.page");
            }
            return PageRequest.of(page - 1, size);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override

    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "productsByCategory", allEntries = true),
            @CacheEvict(value = "searchProducts", allEntries = true)
    })
    public ProductDto saveProduct(ProductDto productDto) {
        if(Objects.nonNull(productDto.getId())){
            throw new RuntimeException("id.must_be.null");
        }

        if(productRepo.existsByName(productDto.getName())){
            throw new RuntimeException("product.name.exist");
        }

        Category category = categoryRepo.findById(productDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("category.not.found"));

        Product product = productMapper.toProduct(productDto);

        if(productDto.getProductDetailsDto() != null){
            ProductDetails details = productMapper.toProductDetails(productDto.getProductDetailsDto());
            details.setProduct(product);
            product.setProductDetails(details);
        }

        Product savedProduct = productRepo.save(product);

        String message = String.format("A new product \"%s\" has been added under the category \"%s\".",
                savedProduct.getName(), category.getName());

        notificationService.handleNotification(null,null,message,"NEW_PRODUCT");

        return productMapper.toProductDto(savedProduct);
    }


    @Override
    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "productsByCategory", allEntries = true),
            @CacheEvict(value = "searchProducts", allEntries = true)
    })
    public List<ProductDto> saveProducts(List<ProductDto> productDtos) {
        List<Product> products = productDtos.stream()
                .map(dto -> {
                    if (Objects.nonNull(dto.getId())) {
                        throw new RuntimeException("id.must_be.null");
                    }
                    if (productRepo.existsByName(dto.getName())) {
                        throw new RuntimeException("product.name.exist");
                    }
                    categoryRepo.findById(dto.getCategoryId())
                            .orElseThrow(() -> new RuntimeException("category.not.found"));

                    Product product = productMapper.toProduct(dto);

                    return product;

                })
                .toList();

        return productMapper.toProductDtoList(productRepo.saveAll(products));
    }


    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "productsByCategory", allEntries = true),
            @CacheEvict(value = "searchProducts", allEntries = true)
    })
    @Override
    public ProductDto addDetails(Long productId, ProductDetailsDto detailsDto) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("product.not.found"));

        if(Objects.nonNull(product.getProductDetails())){
            throw new RuntimeException("details.already.exist");
        }
        ProductDetails details = productMapper.toProductDetails(detailsDto);
        details.setProduct(product);
        product.setProductDetails(details);

        Product updated = productRepo.save(product);
        return productMapper.toProductDto(updated);
    }



    @Override
    @Caching(evict = {
            @CacheEvict(value = "products" , allEntries = true),
            @CacheEvict(value = "productsByCategory", allEntries = true),
            @CacheEvict(value = "searchProducts", allEntries = true)
    })
    public ProductDto updateProduct(ProductDto productDto) {
        if(productDto.getId() == null){
            throw new RuntimeException("id.must_be.not_null");
        }

        Product existingProduct = productRepo.findById(productDto.getId())
                .orElseThrow(() -> new RuntimeException("product.not.found"));

        if (productRepo.existsByNameAndIdNot(productDto.getName(), productDto.getId())) {
            throw new RuntimeException("product.name.exist");
        }

        if (productDto.getCategoryId() != null) {
            categoryRepo.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("category.not.found"));
        }

       // Product product = productMapper.toProduct(productDto);
       // product.setId(existingProduct.getId());

        productMapper.updateProductFromDto(productDto, existingProduct);

        if (productDto.getProductDetailsDto() != null) {
            if (existingProduct.getProductDetails() != null) {
                productMapper.updateProductDetailsFromDto(
                        productDto.getProductDetailsDto(),
                        existingProduct.getProductDetails()
                );
            } else {
                ProductDetails details = productMapper.toProductDetails(productDto.getProductDetailsDto());
                details.setId(null);
                details.setProduct(existingProduct);
                existingProduct.setProductDetails(details);
            }
        }

        return productMapper.toProductDto(productRepo.save(existingProduct));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "productsByCategory", allEntries = true),
            @CacheEvict(value = "searchProducts", allEntries = true)
    })
    public List<ProductDto> updateProducts(List<ProductDto> productDtos) {
        List<Product> products = productDtos.stream()
                .map( dto -> {
                    if (dto.getId() == null) {
                        throw new RuntimeException("id.must_be.not_null");
                    }

                    Product existingProduct = productRepo.findById(dto.getId())
                            .orElseThrow(() -> new RuntimeException("product.not.found"));

                    if (productRepo.existsByNameAndIdNot(dto.getName(), dto.getId())) {
                        throw new RuntimeException("product.name.exist");
                    }

                    if (dto.getCategoryId() != null) {
                        categoryRepo.findById(dto.getCategoryId())
                                .orElseThrow(() -> new RuntimeException("category.not.found"));
                    }
                    productMapper.updateProductFromDto(dto, existingProduct);
                    if (dto.getProductDetailsDto() != null) {
                        if (existingProduct.getProductDetails() != null) {
                            productMapper.updateProductDetailsFromDto(
                                    dto.getProductDetailsDto(),
                                    existingProduct.getProductDetails()
                            );
                        } else {
                            ProductDetails details = productMapper.toProductDetails(dto.getProductDetailsDto());
                            details.setId(null);
                            details.setProduct(existingProduct);
                            existingProduct.setProductDetails(details);
                        }
                    }

                    return existingProduct;
                })
                .toList();
        return productMapper.toProductDtoList(productRepo.saveAll(products));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "productsByCategory", allEntries = true),
            @CacheEvict(value = "searchProducts", allEntries = true)
    })
    public void deleteProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("product.not.found"));
        productRepo.delete(product);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "productsByCategory", allEntries = true),
            @CacheEvict(value = "searchProducts", allEntries = true)
    })
    public void deleteProducts(List<Long> ids) {
        List<Product> products =  productRepo.findAllById(ids);
        if(products.isEmpty()){
            throw new RuntimeException("product.not.found");
        }
        productRepo.deleteAll(products);
    }
}
