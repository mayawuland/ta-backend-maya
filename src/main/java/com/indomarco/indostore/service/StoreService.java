package com.indomarco.indostore.service;

import com.indomarco.indostore.entity.Store;
import com.indomarco.indostore.entity.User;
import com.indomarco.indostore.repository.BranchRepository;
import com.indomarco.indostore.repository.StoreRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import static com.indomarco.indostore.utility.PaginationUtils.paginate;
import java.util.List;

/**
 * Service class for managing Store entities.
 * 
 * Provides functionality to create, read, update, delete (soft delete),
 * and paginate stores. All changes are logged using AuditLogService.
 */
@Service
public class StoreService {
    private final StoreRepository repo;
    private final AuditLogService auditLogService;
    private final BranchRepository branchRepository;

    /**
     * Constructor for StoreService.
     *
     * @param repo The StoreRepository used for database operations.
     * @param auditLogService The AuditLogService used to log changes.
     * @param branchRepository The BranchRepository used to validate branch references.
     */
    public StoreService(StoreRepository repo, AuditLogService auditLogService, BranchRepository branchRepository) {
        this.repo = repo;
        this.auditLogService = auditLogService;
        this.branchRepository = branchRepository;
    }

    /**
     * Creates a new store and logs the creation.
     *
     * @param store The store data to create.
     * @param user The user performing the creation.
     * @return The saved Store entity.
     */
    @Transactional
    public Store create(Store store, User user) {
        if (store.getBranch() == null || store.getBranch().getId() == null) {
            throw new RuntimeException("Branch must be provided");
        }
        var branch = branchRepository.findById(store.getBranch().getId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        store.setBranch(branch);

        Store saved = repo.save(store);
        auditLogService.log("stores", saved.getId(), user, "CREATE", null, saved.toString());
        return saved;
    }

    /**
     * Returns a paginated list of active and not deleted stores.
     *
     * @param page The page number (0-based).
     * @param size The number of items per page.
     * @return A paginated list of stores.
     */
    public List<Store> all(int page, int size) {
        List<Store> stores = repo.findByIsActiveTrueAndIsDeletedFalse();
        return paginate(stores, page, size);
    }

    /**
     * Retrieves a store by its ID.
     *
     * @param id The ID of the store.
     * @return The Store entity.
     */
    public Store get(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Store not found"));
    }

    /**
     * Updates an existing store and logs the changes.
     *
     * @param id The ID of the store to update.
     * @param data The new store data.
     * @param user The user performing the update.
     * @return The updated Store entity.
     */
    @Transactional
    public Store update(Long id, Store data, User user) {
        Store store = get(id);
        String old = store.toString();
        store.setName(data.getName());
        store.setAddress(data.getAddress());
        store.setIsActive(data.getIsActive());
        store.setIsDeleted(data.getIsDeleted());
        
        if (data.getBranch() != null && data.getBranch().getId() != null) {
            var branch = branchRepository.findById(data.getBranch().getId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
            store.setBranch(branch);
        }
        Store updated = repo.save(store);
        auditLogService.log("stores", id, user, "UPDATE", old, updated.toString());
        return updated;
    }

    /**
     * Soft deletes a store by setting {@code isDeleted} to true and logs the deletion.
     *
     * @param id The ID of the store to delete.
     * @param user The user performing the deletion.
     */
    @Transactional
    public void delete(Long id, User user) {
        Store store = get(id);
        store.setIsDeleted(true);
        repo.save(store);
        auditLogService.log("stores", id, user, "DELETE", store.toString(), null);
    }
}

