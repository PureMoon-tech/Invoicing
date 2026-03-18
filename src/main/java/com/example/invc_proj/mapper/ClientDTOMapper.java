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


    public static void updateEntityFromDto(ClientDTO dto, Client entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getClient_name() != null) {
            entity.setClient_name(dto.getClient_name());
        }
        if (dto.getClient_type() != null) {
            entity.setClient_type(dto.getClient_type());
        }
        if (dto.getPrimary_mobile_number() != null) {
            entity.setPrimary_mobile_number(dto.getPrimary_mobile_number());
        }
        if (dto.getPrimary_email_id() != null) {
            entity.setPrimary_email_id(dto.getPrimary_email_id());
        }
        if (dto.getSecondary_mobile_number() != null) {
            entity.setSecondary_mobile_number(dto.getSecondary_mobile_number());
        }
        if (dto.getSecondary_email_id() != null) {
            entity.setSecondary_email_id(dto.getSecondary_email_id());
        }
        if (dto.getOccupation() != null) {
            entity.setOccupation(dto.getOccupation());
        }
        if (dto.getPan_no() != null) {
            entity.setPan_no(dto.getPan_no());
        }
        if (dto.getGSTN() != null) {
            entity.setGSTN(dto.getGSTN());
        }
        if (dto.getAadhaar_no() != null) {
            entity.setAdhaar_no(dto.getAadhaar_no());
        }
        if (dto.getAddress() != null) {
            entity.setAddress(dto.getAddress());
        }
        if (dto.getPincode() != null) {
            entity.setPincode(dto.getPincode());
        }
        if (dto.getAccountNumber() != null) {
            entity.setAccountNumber(dto.getAccountNumber());
        }
        if (dto.getUpiId() != null) {
            entity.setUpiId(dto.getUpiId());
        }

    }

    public static ClientDTO mapToClientDTO(Client client) {
        if (client == null) return null;

        ClientDTO dto = new ClientDTO();
        dto.setClient_id(client.getClient_id());
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
