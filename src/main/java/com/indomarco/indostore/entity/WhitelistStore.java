package com.indomarco.indostore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

/**
 * Represents a whitelist store in the Indostore system.
 * 
 * Each whitelist store is associated with exactly one store.
 * This entity is used to mark stores that should always be visible
 * across all provinces regardless of location.
 */
@Entity
@Table(name = "whitelist_stores")
public class WhitelistStore {
    /** The unique identifier for the whitelist store. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The store associated with this whitelist entry.
     * Back reference for JSON serialization to prevent infinite recursion.
     */
    @OneToOne 
    @JoinColumn(name = "store_id", nullable = false, unique = true)
    @JsonBackReference 
    private Store store;

    /** Getters & Setters */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }
}

