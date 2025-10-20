package com.restaurant.spring.controller;

import com.restaurant.spring.controller.vm.ProductResponseVm;
import com.restaurant.spring.dto.ProductDto;
import com.restaurant.spring.dto.ProductDetailsDto;
import com.restaurant.spring.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin("http://localhost:4200")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("searchByCategory/{categoryId}")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<ProductResponseVm> getProductsByCategoryId(@PathVariable Long categoryId,@RequestParam Integer page, @RequestParam Integer size) {
        return ResponseEntity.ok(productService.getProductsByCategoryId(categoryId,page,size));
    }

    @GetMapping("/getAll")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<ProductResponseVm> getAllProducts(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(productService.getAllProducts(page, size));
    }

    @PostMapping("/save-product")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> saveProduct(@RequestBody @Valid ProductDto  productDto) {
        ProductDto savedProduct = productService.saveProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PostMapping("/save-product/bulk")
    //  @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductDto>> saveProducts(@RequestBody @Valid List<ProductDto> productDtos) {
        List<ProductDto> savedProducts = productService.saveProducts(productDtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProducts);
    }

    @PutMapping("/update-product")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody @Valid ProductDto productDto) {
        ProductDto updatedProduct = productService.updateProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedProduct);
    }

    @PutMapping("/update-product/bulk")
    //  @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductDto>> updateProducts(@RequestBody @Valid List<ProductDto> productDtos) {
        List<ProductDto> updatedProducts = productService.updateProducts(productDtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedProducts);
    }

    @DeleteMapping("/delete")
    //  @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProductById(@RequestParam Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/bulk")
    public ResponseEntity<Void> deleteProducts(@RequestParam List<Long> ids) {
        productService.deleteProducts(ids);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<ProductResponseVm> searchProducts(@RequestParam String key,int page, int size) {
        return ResponseEntity.ok(productService.searchProducts(key,page,size));
    }

    @PostMapping("/{productId}/add-details")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> addDetails(
            @PathVariable Long productId,
            @RequestBody @Valid ProductDetailsDto detailsDto) {
        return ResponseEntity.ok(productService.addDetails(productId, detailsDto));
    }
}
