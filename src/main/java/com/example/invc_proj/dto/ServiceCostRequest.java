package com.example.invc_proj.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServiceCostRequest {
        private int serviceId;        // The ID of the service selected by the user
        private String serviceName;    // The name of the service selected by the user
        private BigDecimal serviceCost; // The cost of the service entered by the user
        private BigDecimal totalCost;  // the total cost of the services entered

    // Getters and Setters

         public ServiceCostRequest(int serviceId,BigDecimal serviceCost,String serviceName,BigDecimal totalCost)
         {
             this.serviceId = serviceId;
             this.serviceName = serviceName;
             this.serviceCost = serviceCost;
             this.totalCost=totalCost;

         }
    }

