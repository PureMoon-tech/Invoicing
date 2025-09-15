package com.example.invc_proj.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "quote_sequence", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"quoteType", "financialYear"})
})
public class QuoteSequence {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String quoteType;
        private String financialYear;
        private int currentSequence;

}
