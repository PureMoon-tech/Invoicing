package com.example.invc_proj.dto;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;


import java.math.BigDecimal;
import java.util.Date;

@Entity
@Immutable
@Table(name = "InvoiceView")
@Getter
@Setter
public class InvoiceView {

    @Id
    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "invoice_number")
    private String invoiceNumber;
    @Column(name = "amount_paid")
    private BigDecimal amountPaid;
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "invoice_generated_date")
    private Date invoiceGeneratedDate;

    @Column(name = "client_name")
    private String clientName;
    @Column(name = "primary_email_id")
    private String primaryEmailId;
    @Column(name = "address")
    private String address;
    @Column(name = "pincode")
    private String pincode;

    @Column(name = "bank_name")
    private String bankName;
    @Column(name = "account_no")
    private String accountNo;
    @Column(name = "account_holder_name")
    private String accountHolderName;
    @Column(name = "ifsc_code")
    private String ifscCode;
    @Column(name = "micr_code")
    private String micrCode;
}
