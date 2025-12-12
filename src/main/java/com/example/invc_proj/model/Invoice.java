package com.example.invc_proj.model;

import com.example.invc_proj.model.Enum.InvoiceStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Invoice {

    @Id
    @Column(name = "invoice_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long invoice_id;

    @Column(name = "invoice_number", unique = true, nullable = false)
    private String invoice_number;

    private int client_id;
    private int user_id;
    private Date invoice_generated_date;
    private Date last_updated_date;
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    private int bank_id;
    private BigDecimal amountPaid;

    @OneToMany(mappedBy = "invoice_id", cascade = CascadeType.ALL)
   // @JsonIgnore
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


