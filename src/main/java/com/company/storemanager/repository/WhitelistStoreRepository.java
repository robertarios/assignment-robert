package com.company.storemanager.repository;

import com.company.storemanager.model.WhitelistStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface WhitelistStoreRepository extends JpaRepository<WhitelistStore, Long> {
    Optional<WhitelistStore> findByStoreIdAndIsActiveTrue(Long storeId);
    List<WhitelistStore> findByIsActiveTrue();
    
    @Modifying
    @Transactional
    @Query("UPDATE WhitelistStore ws SET ws.isActive = false WHERE ws.id = :id")
    void deactivate(@Param("id") Long id);
    
    @Modifying
    @Transactional
    @Query("UPDATE WhitelistStore ws SET ws.isActive = false WHERE ws.store.id = :storeId")
    void deactivateByStoreId(@Param("storeId") Long storeId);
}