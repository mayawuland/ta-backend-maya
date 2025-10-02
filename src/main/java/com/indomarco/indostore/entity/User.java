package com.indomarco.indostore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * Represents a user in the Indostore system.
 * 
 * Each user has a unique email, name, password, and a token.
 * Users can have multiple audit logs associated with their actions.
 */
@Entity
@Table(name = "users")
public class User {
    /** The unique identifier for the user. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The user's email address. Must be unique and valid. */
    @Email @Column(unique = true, nullable = false)
    private String email;

    /** The user's name. Cannot be blank. */
    @NotBlank private String name;

    /** The user's password. Cannot be blank. */
    @NotBlank private String password;
    
    /** Token for authentication purposes. */
    private String token;

    /** List of audit logs associated with this user */
    @OneToMany(mappedBy = "user")
    private List<AuditLog> auditLogs;

    /** Getters & Setters */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public List<AuditLog> getAuditLogs() { return auditLogs; }
    public void setAuditLogs(List<AuditLog> auditLogs) { this.auditLogs = auditLogs; }
}
