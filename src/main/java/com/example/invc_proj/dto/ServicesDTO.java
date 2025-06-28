package com.example.invc_proj.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServicesDTO {

    private String service_name;
    private String service_description;
    private int sac_code;
    private BigDecimal min_price;
    private BigDecimal max_price;
    private int gst_rate;
    private boolean status;

    public ServicesDTO( String service_name, String service_description, int sac_code,
                        BigDecimal min_price, BigDecimal max_price, int gst_rate, boolean status)
    {
        this.service_name = service_name;
        this.service_description = service_description;
        this.sac_code = sac_code;
        this.min_price = min_price;
        this.max_price = max_price;
        this.gst_rate = gst_rate;
        this.status = status;
    }

}
