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
        client.setAccountNumber(dto.getAccountNumber());
        client.setUpiId(dto.getUpiId());

        return client;
    }


    public static ClientDTO mapToClientDTO(Client client) {
        if (client == null) return null;

        ClientDTO dto = new ClientDTO();
        dto.setClient_name(client.getClient_name());
        dto.setClient_type(client.getClient_type());
        dto.setPrimary_mobile_number(client.getPrimary_mobile_number());
        dto.setPrimary_email_id(client.getPrimary_email_id());
        dto.setSecondary_mobile_number(client.getSecondary_mobile_number());
        dto.setSecondary_email_id(client.getSecondary_email_id());
        dto.setOccupation(client.getOccupation());
        dto.setPan_no(client.getPan_no());
        dto.setGSTN(client.getGSTN());
        dto.setAadhaar_no(client.getAdhaar_no());
        dto.setAddress(client.getAddress());
        dto.setPincode(client.getPincode());
        dto.setAccountNumber(client.getAccountNumber());
        dto.setUpiId(client.getUpiId());
        return dto;
    }
}
