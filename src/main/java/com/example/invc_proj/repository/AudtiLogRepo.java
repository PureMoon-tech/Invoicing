package com.example.invc_proj.repository;

import com.example.invc_proj.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudtiLogRepo extends JpaRepository<AuditLog, Long> {
}

