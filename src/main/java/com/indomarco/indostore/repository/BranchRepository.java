package com.indomarco.indostore.repository;

import com.indomarco.indostore.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for Branch entity.
 * 
 * Provides standard CRUD operations and query methods for Branch.
 * 
 * Additionally, this repository defines a custom query method:
 * {@link #findByIsActiveTrueAndIsDeletedFalse()} - returns a list of branches
 * that are active and not deleted.
 */
public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findByIsActiveTrueAndIsDeletedFalse();
}
