package com.indomarco.indostore.entity;

import jakarta.persistence.*;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Represents a province in the Indostore system.
 * 
 * Each province has a name, active status, deletion status, and a list of branches associated with it.
 */
@Entity
@Table(name = "provinces")
public class Province {
    /** The unique identifier for the province. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The name of the province. Cannot be blank. */
    @NotBlank private String name;

    /** Indicates whether the province is active. Defaults to true. */
    private Boolean isActive = true;

    /** Indicates whether the province is deleted. Defaults to false. */
    private Boolean isDeleted = false;

    /** List of branches associated with this province */
    @OneToMany(mappedBy = "province")
    @JsonManagedReference
    private List<Branch> branches;

    /** Getters & Setters */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    public List<Branch> getBranches() { return branches; }
    public void setBranches(List<Branch> branches) { this.branches = branches; }
}
