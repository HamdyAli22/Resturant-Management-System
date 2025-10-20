package com.restaurant.spring.repo;


import com.restaurant.spring.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {

   Page<Product> findAllByOrderByIdAsc(Pageable pageable);

    @Query("select p from Product p where p.category.id = :categoryId order by p.id")
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    Boolean existsByName(String productName);

    boolean existsByNameAndIdNot(String name, Long id);

    @Query("select p from Product  p " +
           "where lower(p.name) like lower(concat('%' ,:name, '%')) " +
           "or lower(p.description) like lower(concat('%' ,:description, '%')) " +
           "order by p.id")
    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description,Pageable pageable);
    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}
