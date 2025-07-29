package com.example.invc_proj.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class BankStatementUpload {

    @Id
    @GeneratedValue
    private Long id;

    private String filename;

    private String fileType; // EXCEL or PDF

    private LocalDateTime uploadedAt;

    private boolean processed = false;

    @Lob
    private byte[] fileData;

    @OneToMany(mappedBy = "statement")
    private List<ReconciliationLog> logs;
}
