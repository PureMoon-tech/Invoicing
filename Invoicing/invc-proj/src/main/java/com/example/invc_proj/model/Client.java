package com.example.invc_proj.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Client {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id", unique = true, nullable = false)
    private int client_id;
    private String client_name;
    private int primary_mobile_number;
    private String primary_email_id;
    private int secondary_mobile_number;
    private String secondary_email_id;
    private String occupation;
    private String pan_no;
    private int adhaar_no;
    private String address;
    private int pincode;
    private String client_type;
    private String GSTN;

}
