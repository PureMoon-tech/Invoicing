package com.example.invc_proj.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id", unique = true, nullable = false)
    private int client_id;

    @Column(name = "client_name", length = 100)
    private String client_name;

    @Column(name = "primary_mobile_number", length = 15)
    private String primary_mobile_number;

    @Column(name = "primary_email_id", length = 100)
   // @Email
    private String primary_email_id;

    @Column(name = "secondary_mobile_number", length = 15)
    private String secondary_mobile_number;

    @Column(name = "secondary_email_id", length = 100)
   // @Email
    private String secondary_email_id;

    @Column(name = "occupation", length = 50)
    private String occupation;

    @Column(name = "pan_no", length = 10)
    private String pan_no;

    @Column(name = "adhaar_no", length = 12)
    private String adhaar_no;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "pincode", length = 6)
    private String pincode;

    @Column(name = "client_type", length = 50)
    private String client_type;

    @Column(name = "GSTN", length = 15)
    private String GSTN;

    public Client(int client_id, String client_name, String client_type) {
        this.client_id = client_id;
        this.client_name = client_name;
        this.client_type = client_type;
    }
}
