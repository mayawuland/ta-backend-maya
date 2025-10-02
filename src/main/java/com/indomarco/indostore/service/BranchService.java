package com.indomarco.indostore.service;

import com.indomarco.indostore.entity.Branch;
import com.indomarco.indostore.entity.Province;
import com.indomarco.indostore.entity.User;
import com.indomarco.indostore.repository.BranchRepository;
import com.indomarco.indostore.repository.ProvinceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import static com.indomarco.indostore.utility.PaginationUtils.paginate;
import java.util.List;

/**
 * Service class for managing Branch entities.
 * 
 * Provides functionality to create, read, update, and delete branches.
 * All changes are recorded in the audit log.
 */
@Service
public class BranchService {
    private final BranchRepository repo;
    private final AuditLogService auditLogService;
    private final ProvinceRepository provinceRepository;

    /**
     * Constructor for BranchService.
     *
     * @param repo The BranchRepository used for database operations.
     * @param auditLogService The AuditLogService used to log changes.
     * @param provinceRepository The ProvinceRepository to validate branch provinces.
     */
    public BranchService(BranchRepository repo, AuditLogService auditLogService, ProvinceRepository provinceRepository) {
        this.repo = repo;
        this.provinceRepository = provinceRepository;
        this.auditLogService = auditLogService;
    }

    /**
     * Creates a new branch and logs the creation.
     *
     * @param branch The branch data to create.
     * @param user The user performing the creation.
     * @return The saved Branch entity.
     */
    @Transactional
    public Branch create(Branch branch, User user) {
        if (branch.getProvince() == null || branch.getProvince().getId() == null) {
            throw new RuntimeException("Province must be provided");
        }

        Province province = provinceRepository.findById(branch.getProvince().getId())
                .orElseThrow(() -> new RuntimeException("Province not found"));
        branch.setProvince(province);
        
        Branch saved = repo.save(branch);
        auditLogService.log("branches", saved.getId(), user, "CREATE", null, saved.toString());
        return saved;
    }

    /**
     * Returns a paginated list of active and not deleted branches.
     *
     * @param page The page number (0-based).
     * @param size The number of items per page.
     * @return A paginated list of branches.
     */
    public List<Branch> all(int page, int size) {
        List<Branch> branches = repo.findByIsActiveTrueAndIsDeletedFalse();
        return paginate(branches, page, size);
    }

    /**
     * Retrieves a branch by its ID.
     *
     * @param id The ID of the branch.
     * @return The Branch entity.
     * @throws RuntimeException If the branch is not found.
     */
    public Branch get(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Branch not found"));
    }

    /**
     * Updates an existing branch and logs the changes.
     *
     * @param id The ID of the branch to update.
     * @param data The new branch data.
     * @param user The user performing the update.
     * @return The updated Branch entity.
     */
    @Transactional
    public Branch update(Long id, Branch data, User user) {
        Branch branch = get(id);
        String old = branch.toString();
        branch.setName(data.getName());
        branch.setIsActive(data.getIsActive());
        branch.setIsDeleted(data.getIsDeleted());
        
        if (data.getProvince() != null && data.getProvince().getId() != null) {
            Province province = provinceRepository.findById(data.getProvince().getId())
                    .orElseThrow(() -> new RuntimeException("Province not found"));
            branch.setProvince(province);
        }

        Branch updated = repo.save(branch);
        auditLogService.log("branches", id, user, "UPDATE", old, updated.toString());
        return updated;
    }

    /**
     * Soft deletes a branch by setting {@code isDeleted} to true and logs the deletion.
     *
     * @param id The ID of the branch to delete.
     * @param user The user performing the deletion.
     * @throws RuntimeException If the branch is not found.
     */
    @Transactional
    public void delete(Long id, User user) {
        Branch branch = get(id);
        branch.setIsDeleted(true);
        repo.save(branch);
        auditLogService.log("branches", id, user, "DELETE", branch.toString(), null);
    }
}

