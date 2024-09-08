package com.example.invc_proj.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor


public class BankDetails {

    @Getter
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bd_id", unique = true, nullable = false)
    private int bn_id;
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
