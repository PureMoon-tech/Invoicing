package com.example.invc_proj.dto;


import com.example.invc_proj.model.Enum.QuoteStatus;
import com.example.invc_proj.model.ServicesQuoted;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuoteResponseDTO {

    private long quote_id;
    private String quote_number;
    private int client_id;
    private int user_id;
    private BigDecimal total;
    private List<ServicesQuotedDTO> quoteSrvcs;
}
