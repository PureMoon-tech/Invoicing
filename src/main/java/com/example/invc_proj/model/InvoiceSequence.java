package com.example.invc_proj.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "invoice_sequence", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"invoiceType", "financialYear"})
})
public class InvoiceSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String invoiceType;
    private String financialYear;
    private int currentSequence;
}
