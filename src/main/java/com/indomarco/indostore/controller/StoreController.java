package com.indomarco.indostore.controller;

import com.indomarco.indostore.entity.Store;
import com.indomarco.indostore.entity.User;
import com.indomarco.indostore.service.StoreService;
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
 * Controller for managing Stores.
 * 
 * Provides endpoints for creating, retrieving, updating, and deleting stores.
 * All endpoints require authentication via the Authorization header.
 */
@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;
    private final UserService userService;

    /**
     * Constructor for StoreController.
     *
     * @param storeService Service for handling store-related operations.
     * @param userService Service for handling user authentication and token validation.
     */
    public StoreController(StoreService storeService, UserService userService) {
        this.storeService = storeService;
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
     * Create a new Store.
     *
     * @param store The Store to create.
     * @param req The HTTP request containing the Authorization header.
     * @return ResponseEntity containing the created Store and message.
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Store store, HttpServletRequest req) {
        try {
            Store created = storeService.create(store, getUser(req));
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Store created successfully",
                    "data", created
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "Failed to create store",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * Retrieve all Stores with pagination.
     *
     * @param page Page number (default 0).
     * @param size Page size (default 50).
     * @param req The HTTP request containing the Authorization header.
     * @return ResponseEntity containing the list of Stores and pagination info.
     */
    @GetMapping
    public ResponseEntity<?> all(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            HttpServletRequest req) {
        try {
            getUser(req);
            List<Store> stores = storeService.all(page, size);
            return ResponseEntity.ok(Map.of(
                    "message", "Stores fetched successfully",
                    "data", stores
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "Failed to fetch stores",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * Retrieve a single Store by ID.
     *
     * @param id The ID of the Store to retrieve.
     * @param req The HTTP request containing the Authorization header.
     * @return ResponseEntity containing the Store and message.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id, HttpServletRequest req) {
        try {
            getUser(req);
            Store store = storeService.get(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Store fetched successfully",
                    "data", store
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", "Failed to fetch store",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * Update an existing Store by ID.
     *
     * @param id The ID of the Store to update.
     * @param store The updated Store data.
     * @param req The HTTP request containing the Authorization header.
     * @return ResponseEntity containing the updated Store and message.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Store store, HttpServletRequest req) {
        try {
            Store updated = storeService.update(id, store, getUser(req));
            return ResponseEntity.ok(Map.of(
                    "message", "Store updated successfully",
                    "data", updated
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "Failed to update store",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * Soft delete a Store by ID.
     *
     * @param id The ID of the Store to delete.
     * @param req The HTTP request containing the Authorization header.
     * @return ResponseEntity containing a success message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest req) {
        try {
            storeService.delete(id, getUser(req));
            return ResponseEntity.ok(Map.of(
                    "message", "Store deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "Failed to delete store",
                    "error", e.getMessage()
            ));
        }
    }
    
}

