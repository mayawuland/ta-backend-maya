package com.indomarco.indostore.repository;

import com.indomarco.indostore.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for Province entity.
 * 
 * Provides standard CRUD operations and query methods for Province.
 * 
 * Additionally, this repository defines custom query methods:
 * {@link #findByIsActiveTrueAndIsDeletedFalse()} - returns all active provinces that are not deleted.
 * {@link #findByNameContainingIgnoreCaseAndIsActiveTrueAndIsDeletedFalse(String)} - returns all active and not deleted provinces whose names contain the given string, ignoring case.
 */
public interface ProvinceRepository extends JpaRepository<Province, Long> {
    List<Province> findByIsActiveTrueAndIsDeletedFalse();
    List<Province> findByNameContainingIgnoreCaseAndIsActiveTrueAndIsDeletedFalse(String name);
}
