package com.example.invc_proj.model;


import com.example.invc_proj.model.Enum.InvoiceStatus;
import com.example.invc_proj.model.Enum.QuoteStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Quote {

    @Id
    @Column(name = "quote_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long quote_id;

    @Column(name = "quote_number", unique = true, nullable = false)
    private String quote_number;

    private int client_id;
    private int user_id;
    private Date quote_generated_date;
    private Date last_updated_date;
    private BigDecimal total;
    private QuoteStatus status;
    @OneToMany(mappedBy = "quote_id", cascade = CascadeType.ALL)
    //@JsonIgnore
    private List<ServicesQuoted> quoteSrvcs;

}

