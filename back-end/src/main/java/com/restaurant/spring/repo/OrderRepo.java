package com.restaurant.spring.repo;

import com.restaurant.spring.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {

        Page<Order> findByAccountIdOrderByIdDesc(Long accountId,Pageable pageable);

        Page<Order> findAllByOrderByIdDesc(Pageable pageable);

        @Query("SELECT o FROM Order o WHERE LOWER(o.account.username) LIKE LOWER(CONCAT('%', :username, '%')) ORDER BY o.id DESC")
        Page<Order> findByUsername(String username, Pageable pageable);

       @Query("""
           SELECT DISTINCT o FROM Order o
           JOIN FETCH o.products p
           WHERE o.account.id = :accountId
             AND (
                 LOWER(o.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
                 OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
             )
           ORDER BY o.id DESC
           """)
      List<Order> searchUserOrdersByKeyword(@Param("accountId") Long accountId,
                                          @Param("keyword") String keyword);
      }
