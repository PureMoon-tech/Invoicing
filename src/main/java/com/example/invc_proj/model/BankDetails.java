package com.example.invc_proj.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "bank_details")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class BankDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_id", unique = true, nullable = false)
    private int bank_id;
    private String account_holder_name;
    private String bank_name;
    private String branch;
    private String account_no;
    private String ifsc_code;
    private String micr_code;
    private String upi_number;
    private Date inserted_on;
    private Date updated_on;
    private String inserted_by;
    private String updated_by;



}
