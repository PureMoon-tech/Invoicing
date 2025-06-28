package com.example.invc_proj.mapper;

import com.example.invc_proj.dto.ClientDTO;
import com.example.invc_proj.model.Client;

public class ClientDTOMapper {


    public static Client mapToClient(ClientDTO dto) {
        if (dto == null) return null;

        Client client = new Client();
        client.setClient_name(dto.getClient_name());
        client.setClient_type(dto.getClient_type());
        client.setPrimary_mobile_number(dto.getPrimary_mobile_number());
        client.setPrimary_email_id(dto.getPrimary_email_id());
        client.setSecondary_mobile_number(dto.getSecondary_mobile_number());
        client.setSecondary_email_id(dto.getSecondary_email_id());
        client.setOccupation(dto.getOccupation());
        client.setPan_no(dto.getPan_no());
        client.setGSTN(dto.getGSTN());
        client.setAdhaar_no(dto.getAadhaar_no());
        client.setAddress(dto.getAddress());
        client.setPincode(dto.getPincode());

        return client;
    }
}
