package com.restaurant.spring.repo.security;

import com.restaurant.spring.model.security.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account,Long> {

    @EntityGraph(attributePaths = "roles")
    Optional<Account> findByUsername(String username);

    Optional<Account> findFirstByRoles_RoleNameIgnoreCase(String roleName);

}
