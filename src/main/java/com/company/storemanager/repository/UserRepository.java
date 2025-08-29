package com.company.storemanager.repository;

import com.company.storemanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameAndIsActiveTrue(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}