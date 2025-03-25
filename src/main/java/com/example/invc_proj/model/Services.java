package com.example.invc_proj.model;

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
public class Services {

    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id", unique = true, nullable = false)
    private int service_id;
    private String service_name;
    private String service_description;
    private BigDecimal min_price;
    private BigDecimal max_price;
    private boolean status;
    private Date inserted_on;
    private Date releaseDate;



}
