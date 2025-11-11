package com.example.invc_proj.mapper;


import com.example.invc_proj.dto.ServicesDropdownDTO;
import com.example.invc_proj.model.Services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ServicesDropdownDTOMapper {

    public static ServicesDropdownDTO toDTO(Services services) {
        if (services == null) {
            return null;
        }

        ServicesDropdownDTO dto = new ServicesDropdownDTO();
        dto.setService_id(services.getService_id());
        dto.setService_name(services.getService_name());
        dto.setService_description(services.getService_description());
        dto.setMin_price(services.getMin_price());
        dto.setMax_price(services.getMax_price());
        return dto;
    }

    public static Services fromDTO(ServicesDropdownDTO dto) {
        if (dto == null) {
            return null;
        }

        Services services = new Services();
        services.setService_id(dto.getService_id());
        services.setService_name(dto.getService_name());
        services.setService_description(dto.getService_description());
        services.setMin_price(dto.getMin_price());
        services.setMax_price(dto.getMax_price());
        return services;
    }

    public static List<ServicesDropdownDTO> toDTOList(List<Services> services) {
        if (services == null) {
            return Collections.emptyList();
        }
        return services.stream()
                .map(ServicesDropdownDTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}
