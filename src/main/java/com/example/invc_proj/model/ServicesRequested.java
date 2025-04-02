package com.example.invc_proj.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ServicesRequested {

    @Id
    @Column(name = "request_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int request_id;
    
    private int service_id;

    @ManyToOne
    @JoinColumn(name = "invoice_id", referencedColumnName = "invoice_id")
    private Invoice invoice_id;

    private int user_id;
    private int client_id;
    private int organization_id;
    private int service_cost;
    private int tds_rate;
    private int gst_rate;
    private int tds_value;
    private int gst_value;
    private BigDecimal service_total;
    private Date last_update_date;


   /* @ManyToOne
    @JoinColumn(name = "invoice_id", insertable = false, updatable = false)
    private Invoice invoice;*/



}
