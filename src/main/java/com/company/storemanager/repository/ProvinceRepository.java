package com.company.storemanager.repository;

import com.company.storemanager.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
    List<Province> findByIsActiveTrue();
    Optional<Province> findByIdAndIsActiveTrue(Long id);
    Optional<Province> findByName(String name);
    Optional<Province> findByNameAndIsActiveTrue(String name);
    
    @Modifying
    @Transactional
    @Query("UPDATE Province p SET p.isActive = false WHERE p.id = :id")
    void softDelete(@Param("id") Long id);
}