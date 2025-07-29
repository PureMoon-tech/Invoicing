package com.example.invc_proj.repository;

import com.example.invc_proj.model.BankStatementUpload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankStatementUploadRepository extends JpaRepository<BankStatementUpload, Long> {
    List<BankStatementUpload> findByProcessedFalse();
}