package com.indomarco.indostore.controller;

import com.indomarco.indostore.entity.Branch;
import com.indomarco.indostore.entity.User;
import com.indomarco.indostore.service.BranchService;
import com.indomarco.indostore.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing Branch entities.
 * Provides endpoints to create, read, update, and delete branches.
 * All endpoints require a valid Authorization token passed in the header.
 */
@RestController
@RequestMapping("/api/branches")
public class BranchController {

    private final BranchService branchService;
    private final UserService userService;

    /**
     * Constructor for BranchController.
     *
     * @param branchService Service for handling branch-related operations.
     * @param userService Service for handling user authentication and token validation.
     */
    public BranchController(BranchService branchService, UserService userService) {
        this.branchService = branchService;
        this.userService = userService;
    }

    /**
     * Helper method to retrieve the authenticated user from the Authorization header.
     *
     * @param req The HTTP request containing the Authorization header.
     * @return Authenticated User object.
     */
    private User getUser(HttpServletRequest req) {
        String token = req.getHeader("Authorization");

        if (token == null || token.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing Authorization header");
        }

        return userService.findByToken(token);
    }

    /**
     * Create a new branch.
     *
     * @param branch Branch object containing branch data.
     * @param req HTTP request for user authentication.
     * @return ResponseEntity with created branch data or error message.
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Branch branch, HttpServletRequest req) {
        try {
            Branch created = branchService.create(branch, getUser(req));
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Branch created successfully",
                    "data", created
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "Failed to create branch",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * Get all active branches with pagination.
     *
     * @param page Page number (default 0)
     * @param size Page size (default 50)
     * @param req HTTP request for user authentication.
     * @return ResponseEntity containing a list of branches or error message.
     */
    @GetMapping
    public ResponseEntity<?> all(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            HttpServletRequest req) {
        try {
            getUser(req);
            List<Branch> branches = branchService.all(page, size);
            return ResponseEntity.ok(Map.of(
                    "message", "Branches fetched successfully",
                    "data", branches
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "Failed to fetch branches",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * Get a branch by its ID.
     *
     * @param id Branch ID
     * @param req HTTP request for user authentication.
     * @return ResponseEntity containing the branch data or error message if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id, HttpServletRequest req) {
        try {
            getUser(req);
            Branch branch = branchService.get(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Branch fetched successfully",
                    "data", branch
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", "Failed to fetch branch",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * Update an existing branch.
     *
     * @param id Branch ID
     * @param branch Branch object with updated data.
     * @param req HTTP request for user authentication.
     * @return ResponseEntity containing updated branch data or error message.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Branch branch, HttpServletRequest req) {
        try {
            Branch updated = branchService.update(id, branch, getUser(req));
            return ResponseEntity.ok(Map.of(
                    "message", "Branch updated successfully",
                    "data", updated
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "Failed to update branch",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * Soft delete a branch by setting its isDeleted flag to true.
     *
     * @param id Branch ID
     * @param req HTTP request for user authentication.
     * @return ResponseEntity with a success message or error message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest req) {
        try {
            branchService.delete(id, getUser(req));
            return ResponseEntity.ok(Map.of(
                    "message", "Branch deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "Failed to delete branch",
                    "error", e.getMessage()
            ));
        }
    }

}

