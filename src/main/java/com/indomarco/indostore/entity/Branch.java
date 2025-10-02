package com.indomarco.indostore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Represents a branch within a province in the Indostore system.
 * 
 * Each branch belongs to one province and can have multiple stores associated with it.
 */
@Entity
@Table(name = "branches")
public class Branch {
    /** The unique identifier for the branch. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The name of the branch. Cannot be blank. */
    @NotBlank private String name;

    /** Indicates whether the branch is active. Defaults to true. */
    private Boolean isActive = true;

    /** Indicates whether the branch is deleted. Defaults to false. */
    private Boolean isDeleted = false;

    /**
     * The province this branch belongs to.
     * Back reference for JSON serialization to prevent infinite recursion.
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false)
    @JsonBackReference 
    private Province province;

    /** List of stores associated with this branch */
    @OneToMany(mappedBy = "branch")
    @JsonManagedReference
    private List<Store> stores;

    /** Getters & Setters */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    public Province getProvince() { return province; }
    public void setProvince(Province province) { this.province = province; }

    public List<Store> getStores() { return stores; }
    public void setStores(List<Store> stores) { this.stores = stores; }
}
