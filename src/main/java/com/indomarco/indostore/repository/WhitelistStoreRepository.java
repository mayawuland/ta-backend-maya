package com.indomarco.indostore.repository;

import com.indomarco.indostore.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for WhitelistStore entity.
 * 
 * Provides standard CRUD operations and query methods for WhitelistStore.
 * 
 * Additionally, this repository defines a custom delete method:
 * {@link #deleteByIdCustom(Long)} - deletes a whitelist store by its ID using a custom query.
 * {@link #existsByStore(Store)} - Checks if a WhitelistStore already exists for a given {@link Store}
 */
public interface WhitelistStoreRepository extends JpaRepository<WhitelistStore, Long> {
    @Modifying
    @Query("DELETE FROM WhitelistStore w WHERE w.id = :id")
    void deleteByIdCustom(@Param("id") Long id);

    boolean existsByStore(Store store);
}

