package com.example.invc_proj.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Invoice {

    @Id
    @Column(name = "invoice_id", unique = true, nullable = false)
    private int invoice_id;
    private int client_id;
    private int user_id;
    private Date invoice_generated_date;
    private Date last_updated_date;
    private int total;
    private String status;

    @OneToMany(mappedBy = "invoice_id", cascade = CascadeType.ALL)
    private List<ServicesRequested> invcSrvcs;

}







    /*
    private Invoice(int invoice_id, ServicesRequested[] invcSrvcs) {

        this.invoice_id = invoice_id;
        this.invcSrvcs = invcSrvcs;

         public ServicesRequested[] getInvcSrvcs() {
        return invcSrvcs;
    }

    public void setInvcSrvcs(ServicesRequested[] invcSrvcs) {
        this.invcSrvcs = invcSrvcs;
    }

    }
}

        */


