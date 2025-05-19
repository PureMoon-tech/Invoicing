package com.example.invc_proj.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_id", unique = true, nullable = false)
    private int receipt_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    private long amount;
    private LocalDateTime payment_date;
    private String payment_mode;
    private String reference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acknowledged_by", nullable = false)
    private User acknowledgedBy;

    private String remarks;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime inserted_on;

    @UpdateTimestamp
    private LocalDateTime updated_on;
}
