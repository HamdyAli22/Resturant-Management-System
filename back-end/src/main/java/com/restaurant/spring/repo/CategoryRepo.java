package com.restaurant.spring.repo;

import com.restaurant.spring.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {

    Page<Category> findAllByOrderByNameAsc(Pageable pageable);
    List<Category> findAllByOrderByNameAsc();
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    Page<Category> findByNameContainingIgnoreCaseOrFlagContainingIgnoreCaseOrderByNameAsc(String name, String flag, Pageable pageable);

}
