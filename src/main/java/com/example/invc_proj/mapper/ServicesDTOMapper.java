package com.example.invc_proj.mapper;

import com.example.invc_proj.dto.ServicesDTO;
import com.example.invc_proj.model.Services;

public class ServicesDTOMapper {


        public static Services mapToService(ServicesDTO dto) {
            if (dto == null) return null;

            Services service = new Services();
            service.setService_name(dto.getService_name());
            service.setService_description(dto.getService_description());
            service.setSac_code(dto.getSac_code());
            service.setMin_price(dto.getMin_price());
            service.setMax_price(dto.getMax_price());
            service.setGst_rate(dto.getGst_rate());
            service.setStatus(dto.isStatus());

            return service;
        }
    }


