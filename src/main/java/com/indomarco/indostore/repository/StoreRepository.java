package com.indomarco.indostore.repository;

import com.indomarco.indostore.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for Store entity.
 * 
 * Provides standard CRUD operations and query methods for Store.
 * 
 * Additionally, this repository defines a custom query method:
 * {@link #findByIsActiveTrueAndIsDeletedFalse()} - returns a list of stores
 * that are active and not deleted.
 */
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByIsActiveTrueAndIsDeletedFalse();
}
