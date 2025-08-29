package com.company.storemanager.repository;

import com.company.storemanager.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findByIsActiveTrueAndIsDeletedFalse();
    Optional<Branch> findByIdAndIsActiveTrueAndIsDeletedFalse(Long id);
    Optional<Branch> findByCodeAndIsActiveTrueAndIsDeletedFalse(String code);
    
    @Modifying
    @Transactional
    @Query("UPDATE Branch b SET b.isActive = false, b.isDeleted = true WHERE b.id = :id")
    void softDelete(@Param("id") Long id);
    
    // Tambahkan method untuk update juga
    @Modifying
    @Transactional
    @Query("UPDATE Branch b SET b.name = :name, b.code = :code WHERE b.id = :id")
    void updateBranch(@Param("id") Long id, @Param("name") String name, @Param("code") String code);
}