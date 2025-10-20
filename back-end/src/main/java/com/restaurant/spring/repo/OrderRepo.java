package com.restaurant.spring.repo;

import com.restaurant.spring.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {
    List<Order> findByAccountIdOrderByIdDesc(Long accountId);
        Page<Order> findAllByOrderByIdDesc(Pageable pageable);
        @Query("SELECT o FROM Order o WHERE LOWER(o.account.username) LIKE LOWER(CONCAT('%', :username, '%')) ORDER BY o.id DESC")
        Page<Order> findByUsername(String username, Pageable pageable);
}
