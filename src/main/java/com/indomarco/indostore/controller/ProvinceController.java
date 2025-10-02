package com.indomarco.indostore.controller;

import com.indomarco.indostore.entity.Province;
import com.indomarco.indostore.entity.User;
import com.indomarco.indostore.service.ProvinceService;
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
 * Controller for managing Provinces and retrieving Stores by Province.
 * 
 * Provides endpoints for creating, reading, updating, and deleting provinces,
 * as well as searching provinces by name and fetching stores by province.
 * All endpoints require an Authorization token.
 */
@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {

    private final ProvinceService provinceService;
    private final UserService userService;

    /**
     * Constructor for ProvinceController.
     *
     * @param provinceService Service for handling province-related operations.
     * @param userService Service for handling user authentication and token validation.
     */
    public ProvinceController(ProvinceService provinceService, UserService userService) {
        this.provinceService = provinceService;
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
     * Create a new Province.
     *
     * @param province The Province to create.
     * @param req The HTTP request containing the Authorization header.
     * @return ResponseEntity containing the created Province and message.
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Province province, HttpServletRequest req) {
        try {
            Province created = provinceService.create(province, getUser(req));
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Province created successfully",
                    "data", created
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "Failed to create province",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * Fetch all active provinces with pagination.
     *
     * @param page Page index (default 0).
     * @param size Page size (default 50).
     * @param req The HTTP request containing the Authorization header.
     * @return ResponseEntity containing a list of Provinces and message.
     */
    @GetMapping
    public ResponseEntity<?> all(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            HttpServletRequest req) {
        try {
            getUser(req);
            List<Province> provinces = provinceService.all(page, size);
            return ResponseEntity.ok(Map.of(
                    "message", "Provinces fetched successfully",
                    "data", provinces
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "Failed to fetch provinces",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * Fetch a single Province by ID.
     *
     * @param id Province ID.
     * @param req The HTTP request containing the Authorization header.
     * @return ResponseEntity containing the Province and message.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id, HttpServletRequest req) {
        try {
            getUser(req);
            Province province = provinceService.get(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Province fetched successfully",
                    "data", province
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", "Failed to fetch province",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * Update an existing Province by ID.
     *
     * @param id Province ID.
     * @param province Updated Province data.
     * @param req The HTTP request containing the Authorization header.
     * @return ResponseEntity containing the updated Province and message.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Province province, HttpServletRequest req) {
        try {
            Province updated = provinceService.update(id, province, getUser(req));
            return ResponseEntity.ok(Map.of(
                    "message", "Province updated successfully",
                    "data", updated
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "Failed to update province",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * Soft delete a Province by ID.
     *
     * @param id Province ID.
     * @param req The HTTP request containing the Authorization header.
     * @return ResponseEntity containing a success message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest req) {
        try {
            provinceService.delete(id, getUser(req));
            return ResponseEntity.ok(Map.of(
                    "message", "Province deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "Failed to delete province",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * Search provinces by name with pagination.
     *
     * @param name Name to search for.
     * @param page Page index (default 0).
     * @param size Page size (default 50).
     * @param req The HTTP request containing the Authorization header.
     * @return ResponseEntity containing the search results and message.
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            HttpServletRequest req) {
        try {
            getUser(req);
            List<Province> results = provinceService.searchByName(name, page, size);
            return ResponseEntity.ok(Map.of(
                    "message", "Provinces fetched successfully",
                    "data", results
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "Failed to search provinces",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * Search stores by province name, including whitelist stores, with pagination.
     *
     * @param name Province name to search stores for.
     * @param page Page index (default 0).
     * @param size Page size (default 50).
     * @param req The HTTP request containing the Authorization header.
     * @return ResponseEntity containing stores and message.
     */
    @GetMapping("/search/stores")
    public ResponseEntity<?> searchStoresByProvince(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            HttpServletRequest req) {
        try {
            getUser(req);
            Map<String, Object> results = provinceService.searchStoresByProvince(name, page, size);
            return ResponseEntity.ok(Map.of(
                    "message", "Stores fetched successfully by province",
                    "data", results
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "Failed to fetch stores by province",
                    "error", e.getMessage()
            ));
        }
    }
    
}