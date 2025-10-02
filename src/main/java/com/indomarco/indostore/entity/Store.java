package com.indomarco.indostore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a store within a branch in the Indostore system.
 * 
 * Each store belongs to one branch and can optionally be part of a whitelist.
 */
@Entity
@Table(name = "stores")
public class Store {
    /** The unique identifier for the store. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The name of the store. Cannot be blank. */
    @NotBlank private String name;

    /** The address of the store. Cannot be blank. */
    @NotBlank private String address;

    /** Indicates whether the store is active. Defaults to true. */
    private Boolean isActive = true;

    /** Indicates whether the store is deleted. Defaults to false. */
    private Boolean isDeleted = false;

     /**
     * The branch this store belongs to.
     * Back reference for JSON serialization to prevent infinite recursion.
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    @JsonBackReference
    private Branch branch;

    /** Optional whitelist store information */
    @OneToOne(mappedBy = "store")
    @JsonManagedReference
    private WhitelistStore whitelistStore;

    /** Getters & Setters */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    public Branch getBranch() { return branch; }
    public void setBranch(Branch branch) { this.branch = branch; }

    public WhitelistStore getWhitelistStore() { return whitelistStore; }
    public void setWhitelistStore(WhitelistStore whitelistStore) { this.whitelistStore = whitelistStore; }
}
