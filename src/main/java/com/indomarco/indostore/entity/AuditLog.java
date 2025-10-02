package com.indomarco.indostore.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents an audit log entry in the Indostore system.
 * 
 * Each audit log records changes made to a database table,
 * including the old and new values, the action performed,
 * the timestamp, and the user who performed the action.
 */
@Entity
@Table(name = "audit_log")
public class AuditLog {
    /** The unique identifier for the audit log entry. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The name of the table where the change occurred. */
    private String tableName;

    /** The ID of the record that was changed. */
    private Long recordId;

    /** The action performed (e.g., CREATE, UPDATE, DELETE). */
    private String action;

    /** The timestamp when the action was performed. */
    private LocalDateTime timestamp;

    /** The previous value of the record (stored as text). */
    @Column(columnDefinition = "TEXT")
    private String oldValue;
    
    /** The new value of the record (stored as text). */
    @Column(columnDefinition = "TEXT")
    private String newValue;

     /** The user who performed the action. Cannot be null. */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Getters & Setters */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getOldValue() { return oldValue; }
    public void setOldValue(String oldValue) { this.oldValue = oldValue; }

    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
