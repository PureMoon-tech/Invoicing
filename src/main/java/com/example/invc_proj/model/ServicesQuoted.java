package com.example.invc_proj.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServicesQuoted {

        @Id
        @Column(name = "request_id", unique = true, nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int request_id;

        private int service_id;

        @ManyToOne
        @JoinColumn(name = "quote_id", referencedColumnName = "quote_id")
        @JsonIgnore
        private Quote quote_id;

        private int user_id;
        private int client_id;
        private BigDecimal service_cost;
        private int tds_rate;
        private int gst_rate;
        private BigDecimal tds_value;
        private BigDecimal gst_value;
        private BigDecimal service_total;
        private Date last_update_date;

    }

