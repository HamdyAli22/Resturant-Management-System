package com.restaurant.spring.repo;

import com.restaurant.spring.model.Chef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChefRepo extends JpaRepository<Chef, Long> {
   List<Chef> findAllByOrderByIdAsc();
}
