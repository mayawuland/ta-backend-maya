package com.indomarco.indostore.service;

import com.indomarco.indostore.entity.WhitelistStore;
import com.indomarco.indostore.entity.Store;
import com.indomarco.indostore.entity.User;
import com.indomarco.indostore.repository.StoreRepository;
import com.indomarco.indostore.repository.WhitelistStoreRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import static com.indomarco.indostore.utility.PaginationUtils.paginate;
import java.util.List;

/**
 * Service class for managing WhitelistStore entities.
 * 
 * Provides functionality for creating, listing, updating, and deleting whitelist stores.
 */
@Service
public class WhitelistStoreService {
    private final WhitelistStoreRepository repo;
    private final StoreRepository storeRepository;
    private final AuditLogService auditLogService;

    /**
     * Constructor for WhitelistStoreService.
     *
     * @param repo The WhitelistStoreRepository used for database operations.
     * @param auditLogService The AuditLogService used to log changes.
     * @param storeRepository The StoreRepository used to validate store references.
     */
    public WhitelistStoreService(WhitelistStoreRepository repo, StoreRepository storeRepository,
                                 AuditLogService auditLogService) {
        this.repo = repo;
        this.storeRepository = storeRepository;
        this.auditLogService = auditLogService;
    }

    /**
     * Creates a new WhitelistStore and associates it with a store.
     *
     * @param whiteliststore  The WhitelistStore entity to create.
     * @param user The user performing the action.
     * @return The saved WhitelistStore.
     */
    @Transactional
    public WhitelistStore create(WhitelistStore whiteliststore, User user) {
        Store store = storeRepository.findById(whiteliststore.getStore().getId())
            .orElseThrow(() -> new RuntimeException("Store not found"));
        
        boolean exists = repo.existsByStore(store);
        if (exists) {
            throw new RuntimeException("Store is already whitelisted");
        }
        whiteliststore.setStore(store);
        WhitelistStore saved = repo.save(whiteliststore);
        auditLogService.log("whitelist_stores", saved.getStore().getId(), user, "CREATE", null, saved.toString());
        return saved;
    }

    /**
     * Returns a paginated list of active whitelist stores.
     *
     * @param page The page number (starting from 0).
     * @param size The page size.
     * @return List of stores in the whitelist.
     */
    public List<Store> all(int page, int size) {
        List<Store> whitelistStores = repo.findAll().stream()
            .map(whiteliststore -> whiteliststore.getStore())
            .filter(store -> store.getIsActive() && !store.getIsDeleted())
            .toList();
        return paginate(whitelistStores, page, size);
    }

    /**
     * Retrieves a WhitelistStore by its ID.
     *
     * @param id The ID of the whitelist store.
     * @return The WhitelistStore entity.
     */
    public WhitelistStore get(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Whitelist store not found"));
    }

    /**
     * Updates an existing WhitelistStore with a new associated store.
     *
     * @param id   The ID of the whitelist store to update.
     * @param data The updated WhitelistStore data.
     * @param user The user performing the update.
     * @return The updated WhitelistStore.
     */
    @Transactional
    public WhitelistStore update(Long id, WhitelistStore data, User user) {
        WhitelistStore existing = get(id);

        Store newStore = storeRepository.findById(data.getStore().getId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

        String old = existing.toString();

        existing.setStore(newStore);

        WhitelistStore updated = repo.save(existing);

        auditLogService.log("whitelist_stores", id, user, "UPDATE", old, updated.toString());
        return updated;
    }

    /**
     * Deletes a WhitelistStore by ID (custom delete query).
     *
     * @param id   The ID of the whitelist store to delete.
     * @param user The user performing the deletion.
     */
    @Transactional
    public void delete(Long id, User user) {
        repo.deleteByIdCustom(id);
        auditLogService.log("whitelist_stores", id, user, "DELETE", null, null);
    }

}

