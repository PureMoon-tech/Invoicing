package com.example.invc_proj.dto;

import com.example.invc_proj.model.Enum.QuoteStatus;
import com.example.invc_proj.model.Enum.QuoteType;
import lombok.Data;

import java.util.List;

@Data
public class QuoteRequestDTO {
    private Integer p_client_Id;
    private QuoteStatus p_quote_status;
    private QuoteType p_quote_type;
    private List<ServiceCostRequest> serviceCostRequest;
}
