package com.example.invc_proj.mapper;

import com.example.invc_proj.dto.InvoiceResponseDTO;
import com.example.invc_proj.model.Invoice;

public class InvoiceResponseDTOMapper {

    public static InvoiceResponseDTO toDTO(Invoice invoice) {
        InvoiceResponseDTO dto = new InvoiceResponseDTO();
         dto.setInvoiceId(invoice.getInvoice_id());
         dto.setInvoiceNumber(invoice.getInvoice_number());
        return dto;
    }
}
