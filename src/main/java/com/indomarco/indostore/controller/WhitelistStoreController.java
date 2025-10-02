package com.indomarco.indostore.controller;

import com.indomarco.indostore.entity.Store;
import com.indomarco.indostore.entity.User;
import com.indomarco.indostore.entity.WhitelistStore;
import com.indomarco.indostore.service.WhitelistStoreService;
import com.indomarco.indostore.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing Whitelist Stores.
 * 
 * Provides endpoints for creating, retrieving, updating, and deleting whitelist stores.
 * All endpoints require authentication via the Authorization header.
 */
@RestController
@RequestMapping("/api/whitelist-stores")
public class WhitelistStoreController {

    private final WhitelistStoreService whitelistService;
    private final UserService userService;

    /**
     * Constructor for WhitelistStoreController.
     *
     * @param whitelistService Service for handling whitelist store-related operations.
     * @param userService Service for handling user authentication and token validation.
     */
    public WhitelistStoreController(WhitelistStoreService whitelistService, UserService userService) {
        this.whitelistService = whitelistService;
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
     * Create a new WhitelistStore.
     *
     * @param whitelistStore The WhitelistStore to create.
     * @param req The HTTP request containing the Authorization header.
     * @return ResponseEntity containing the created WhitelistStore and message.
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody WhitelistStore whitelistStore, HttpServletRequest req) {
        try {
            WhitelistStore saved = whitelistService.create(whitelistStore, getUser(req));
            return ResponseEntity.ok(Map.of(
                    "message", "Whitelist store created successfully",
                    "data", saved
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "Failed to create whitelist store",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * Retrieve all WhitelistStores with pagination.
     *
     * @param page Page number (default 0).
     * @param size Page size (default 50).
     * @param req The HTTP request containing the Authorization header.
     * @return ResponseEntity containing the list of Stores and message.
     */
    @GetMapping
    public ResponseEntity<?> all(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            HttpServletRequest req) {
        try {
            getUser(req);
            List<Store> list = whitelistService.all(page, size);
            return ResponseEntity.ok(Map.of(
                    "message", "Whitelist stores retrieved successfully",
                    "data", list
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "Failed to retrieve whitelist stores",
                    "error", e.getMessage()
            ));
        }
    }

     /**
     * Update an existing WhitelistStore by ID.
     *
     * @param id The ID of the WhitelistStore to update.
     * @param whitelistStore The updated WhitelistStore data.
     * @param req The HTTP request containing the Authorization header.
     * @return ResponseEntity containing the updated WhitelistStore and message.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody WhitelistStore whitelistStore,
            HttpServletRequest req
    ) {
        try {
            WhitelistStore updated = whitelistService.update(id, whitelistStore, getUser(req));
            return ResponseEntity.ok(Map.of(
                    "message", "Whitelist store updated successfully",
                    "data", updated
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "Failed to update whitelist store",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * Delete a WhitelistStore by ID.
     *
     * @param id The ID of the WhitelistStore to delete.
     * @param req The HTTP request containing the Authorization header.
     * @return ResponseEntity containing a success message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest req) {
        try {
            whitelistService.delete(id, getUser(req));
            return ResponseEntity.ok(Map.of(
                    "message", "Whitelist store deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "Failed to delete whitelist store",
                    "error", e.getMessage()
            ));
        }
    }

}

