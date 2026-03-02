package com.example.invc_proj.mapper;

import com.example.invc_proj.dto.ServicesQuotedDTO;
import com.example.invc_proj.model.Quote;
import com.example.invc_proj.model.ServicesQuoted;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ServicesQuotedDTOMapper {

    public static ServicesQuotedDTO toDTO(ServicesQuoted services)
    {
        if(services == null)
            return null;

     ServicesQuotedDTO servicesQuotedDTO = new ServicesQuotedDTO();

          servicesQuotedDTO.setService_id(services.getService_id());
          servicesQuotedDTO.setService_cost(services.getService_cost());
          servicesQuotedDTO.setTds_rate(services.getTds_rate());
          servicesQuotedDTO.setGst_rate(services.getGst_rate());
          servicesQuotedDTO.setTds_value(services.getTds_value());
          servicesQuotedDTO.setGst_value(services.getGst_value());
          servicesQuotedDTO.setService_total(services.getService_total());

          return servicesQuotedDTO;
    }


    public static List<ServicesQuotedDTO> toDTOList(List<ServicesQuoted> servicesQuotes)
    {
        if(servicesQuotes==null || servicesQuotes.isEmpty())
        {
            return Collections.emptyList();
        }

        List<ServicesQuotedDTO> servicesQuotedDTOList = new ArrayList<>();
        return servicesQuotes.stream()
                .map(ServicesQuotedDTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}
