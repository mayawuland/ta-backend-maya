package com.indomarco.indostore.service;

import com.indomarco.indostore.entity.AuditLog;
import com.indomarco.indostore.entity.User;
import com.indomarco.indostore.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * Service class for managing audit logs in the Indostore system.
 * 
 * Provides functionality to create and save audit log entries whenever
 * a database record is created, updated, or deleted.
 */
@Service
public class AuditLogService {
    private final AuditLogRepository repo;

    /** Constructor for AuditLogService */
    public AuditLogService(AuditLogRepository repo) {
        this.repo = repo;
    }

    /** Creates and saves an audit log entry */
    public void log(String table, Long recordId, User user, String action, String oldValue, String newValue) {
        AuditLog log = new AuditLog();
        log.setTableName(table);
        log.setRecordId(recordId);
        log.setUser(user);
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        repo.save(log);
    }
}
