package com.example.invc_proj.repository;

import com.example.invc_proj.model.ReconciliationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReconciliationLogRepository extends JpaRepository<ReconciliationLog, Long> {
}