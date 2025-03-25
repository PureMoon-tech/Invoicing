package com.example.invc_proj.model;


import jakarta.persistence.*;
import lombok.*;

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
    private int request_id;
    
    private int service_id;
    private int invoice_id;
    private int user_id;
    private int client_id;
    private int organization_id;
    private int service_cost;
    private int tds_rate;
    private int gst_rate;
    private int tds_value;
    private int gst_value;
    private int service_total;
    private Date last_update_date;


   /* @ManyToOne
    @JoinColumn(name = "invoice_id", insertable = false, updatable = false)
    private Invoice invoice;*/

}
