package com.example.invc_proj.mapper;

import com.example.invc_proj.dto.InvoiceResponseDTO;
import com.example.invc_proj.model.Invoice;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceResponseDTOMapper {

    public static InvoiceResponseDTO toDTO(Invoice entity) {
        if (entity == null) {
            return null;
        }
        InvoiceResponseDTO dto = new InvoiceResponseDTO();
        dto.setInvoiceId(entity.getInvoice_id());
        dto.setInvoiceNumber(entity.getInvoice_number());
        dto.setClientId(entity.getClient_id());
        dto.setTotal(entity.getTotal());
        dto.setStatus(entity.getStatus());
        dto.setBankId(entity.getBank_id());
        dto.setAmountPaid(entity.getAmountPaid());
        dto.setInvoiceGeneratedDate(entity.getInvoice_generated_date());
        return dto;
    }

    public static Invoice toEntity(InvoiceResponseDTO dto) {
        if (dto == null) {
            return null;
        }
        Invoice entity = new Invoice();
        entity.setInvoice_id(dto.getInvoiceId());
        entity.setInvoice_number(dto.getInvoiceNumber());
        entity.setClient_id(dto.getClientId());
        entity.setTotal(dto.getTotal());
        entity.setStatus(dto.getStatus());
        entity.setBank_id(dto.getBankId());
        entity.setAmountPaid(dto.getAmountPaid());
        entity.setInvoice_generated_date(dto.getInvoiceGeneratedDate());
        return entity;
    }

    public static List<InvoiceResponseDTO> toDTOList(List<Invoice> invoices) {
        if (invoices == null) {
            return Collections.emptyList();
        }
        return invoices.stream()
                .map(InvoiceResponseDTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}
