package com.example.invc_proj.model;


import com.example.invc_proj.model.Enum.InvoiceStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ReconciliationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionRef;

    private Long matchedInvoiceId;

    private InvoiceStatus status;

    private String error;

    @ManyToOne
    private BankStatementUpload statement;
}
