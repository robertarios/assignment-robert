package com.company.storemanager.repository;

import com.company.storemanager.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByIsActiveTrueAndIsDeletedFalse();
    Optional<Store> findByIdAndIsActiveTrueAndIsDeletedFalse(Long id);
    
    @Query("SELECT s FROM Store s WHERE s.branch.province.name = :provinceName AND s.isActive = true AND s.isDeleted = false")
    List<Store> findByProvinceName(@Param("provinceName") String provinceName);
    
    @Query("SELECT s FROM Store s WHERE s.id IN (SELECT ws.store.id FROM WhitelistStore ws WHERE ws.isActive = true) AND s.isActive = true AND s.isDeleted = false")
    List<Store> findWhitelistedStores();
    
    @Modifying
    @Transactional
    @Query("UPDATE Store s SET s.isActive = false, s.isDeleted = true WHERE s.id = :id")
    void softDelete(@Param("id") Long id);
}