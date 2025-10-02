package com.indomarco.indostore.service;

import com.indomarco.indostore.entity.Province;
import com.indomarco.indostore.entity.Store;
import com.indomarco.indostore.entity.User;
import com.indomarco.indostore.repository.ProvinceRepository;
import com.indomarco.indostore.repository.WhitelistStoreRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import static com.indomarco.indostore.utility.PaginationUtils.paginate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for managing Province entities.
 * 
 * Provides functionality to create, read, update, delete (soft delete), 
 * and search provinces. Also provides search for stores by province 
 * including whitelist stores. All changes are logged using AuditLogService.
 */
@Service
public class ProvinceService {
    private final ProvinceRepository repo;
    private final AuditLogService auditLogService;
    private final WhitelistStoreRepository whitelistRepo;

    /**
     * Constructor for ProvinceService.
     *
     * @param repo The ProvinceRepository used for database operations.
     * @param auditLogService The AuditLogService used to log changes.
     * @param whitelistRepo The WhitelistStoreRepository used to retrieve whitelist stores.
     */
    public ProvinceService(ProvinceRepository repo, AuditLogService auditLogService,
                           WhitelistStoreRepository whitelistRepo) {
        this.repo = repo;
        this.auditLogService = auditLogService;
        this.whitelistRepo = whitelistRepo;
    }

    /**
     * Creates a new province and logs the creation.
     *
     * @param province The province data to create.
     * @param user The user performing the creation.
     * @return The saved Province entity.
     */
    @Transactional
    public Province create(Province province, User user) {
        Province saved = repo.save(province);
        auditLogService.log("provinces", saved.getId(), user, "CREATE", null, saved.toString());
        return saved;
    }

    /**
     * Returns a paginated list of active and not deleted provinces.
     *
     * @param page The page number (0-based).
     * @param size The number of items per page.
     * @return A paginated list of provinces.
     */
    public List<Province> all(int page, int size) {
        List<Province> provinces = repo.findByIsActiveTrueAndIsDeletedFalse();
        return paginate(provinces, page, size);
    }

    /**
     * Retrieves a province by its ID.
     *
     * @param id The ID of the province.
     * @return The Province entity.
     * @throws RuntimeException If the province is not found.
     */
    public Province get(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Province not found"));
    }

    /**
     * Updates an existing province and logs the changes.
     *
     * @param id The ID of the province to update.
     * @param data The new province data.
     * @param user The user performing the update.
     * @return The updated Province entity.
     */
    @Transactional
    public Province update(Long id, Province data, User user) {
        Province province = get(id);
        String old = province.toString();
        province.setName(data.getName());
        province.setIsActive(data.getIsActive());
        province.setIsDeleted(data.getIsDeleted());
        Province updated = repo.save(province);
        auditLogService.log("provinces", id, user, "UPDATE", old, updated.toString());
        return updated;
    }

    /**
     * Soft deletes a province by setting {@code isDeleted} to true and logs the deletion.
     *
     * @param id The ID of the province to delete.
     * @param user The user performing the deletion.
     */
    @Transactional
    public void delete(Long id, User user) {
        Province province = get(id);
        province.setIsDeleted(true);
        repo.save(province);
        auditLogService.log("provinces", id, user, "DELETE", province.toString(), null);
    }

     /**
     * Searches provinces by name with pagination.
     *
     * @param name The name to search for (case-insensitive, partial match).
     * @param page The page number (0-based).
     * @param size The number of items per page.
     * @return A paginated list of provinces matching the name.
     */
    public List<Province> searchByName(String name, int page, int size) {
        List<Province> provinces = repo.findByNameContainingIgnoreCaseAndIsActiveTrueAndIsDeletedFalse(name);
        return paginate(provinces, page, size);
    }

    /**
     * Searches stores by province name including whitelist stores, with pagination.
     *
     * @param provinceName The name of the province to search stores in.
     * @param page The page number (0-based).
     * @param size The number of items per page.
     * @return A map containing "provinceStores" and "whitelistStores" as paginated lists of stores.
     */
    public Map<String, Object> searchStoresByProvince(String provinceName, int page, int size) {
        Province province = repo.findByNameContainingIgnoreCaseAndIsActiveTrueAndIsDeletedFalse(provinceName)
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Province not found"));

        List<Store> provinceStores = province.getBranches().stream()
                .flatMap(branch -> branch.getStores().stream())
                .filter(store -> store.getIsActive() && !store.getIsDeleted())
                .toList();

        List<Store> whitelistStores = whitelistRepo.findAll().stream()
                .map(whiteliststore -> whiteliststore.getStore())
                .filter(store -> store.getIsActive() && !store.getIsDeleted())
                .toList();

        List<Store> paginatedProvinceStores = paginate(provinceStores, page, size);
        List<Store> paginatedWhitelistStores = paginate(whitelistStores, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("whitelistStores", paginatedWhitelistStores);
        response.put("provinceStores", paginatedProvinceStores);

        return response;
    }

}