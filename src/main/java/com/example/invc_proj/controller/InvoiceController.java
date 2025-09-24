package com.example.invc_proj.controller;

import com.example.invc_proj.dto.InvoiceRequestDTO;
import com.example.invc_proj.dto.InvoiceUpdateDTO;
import com.example.invc_proj.dto.ServiceCostRequest;
import com.example.invc_proj.model.Enum.InvoiceStatus;
import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.model.Enum.InvoiceType;
import com.example.invc_proj.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/invoice")
@PreAuthorize("isAuthenticated()")
public class InvoiceController {

    @Autowired
    private InvoiceService service;

    /*End point to generate invoice*/
    @PostMapping("/generate-invoice/{p_client_Id}/{p_bank_id}/{p_invoice_status}/{p_invoice_type}/{p_amount_paid}")
    public ResponseEntity<Invoice> generateInvoice (@PathVariable Integer p_client_Id,
                               @PathVariable Integer p_bank_id,
                               @PathVariable InvoiceStatus p_invoice_status,
                               @PathVariable InvoiceType p_invoice_type,
                               @PathVariable BigDecimal p_amount_paid,
                               @RequestBody List<ServiceCostRequest> serviceCostRequest)
    {
        Invoice invoice = service.generateInvoice(p_client_Id,p_bank_id,p_invoice_status,p_invoice_type,p_amount_paid,serviceCostRequest);
        return ResponseEntity.status(201).body(invoice);
    }

    /*End point to generate invoice*/
    @PostMapping("/generate-invoice")
    public ResponseEntity<Invoice> generateInvoice (
            @RequestBody InvoiceRequestDTO invoiceRequestDTO)
    {
        Invoice invoice = service.generateInvoice(invoiceRequestDTO.getClient_Id(),
                invoiceRequestDTO.getBank_id(),invoiceRequestDTO.getInvoice_status(),
                invoiceRequestDTO.getInvoice_type(),invoiceRequestDTO.getAmount_paid(),
                invoiceRequestDTO.getServiceCostRequest());
        return ResponseEntity.status(201).body(invoice);
    }


    /*end point to update invoice status*/
    @PutMapping("/update-status/{p_invoice_id}/{p_invoice_status}/{p_amount_paid}")
    public ResponseEntity<Invoice> updateInvoiceStatus(@PathVariable Long p_invoice_id,
                                       @PathVariable InvoiceStatus p_invoice_status,
                                       @PathVariable BigDecimal p_amount_paid)
    {
         Invoice invoice = service.updateInvoiceStatus(p_invoice_id,p_invoice_status,p_amount_paid);
          return ResponseEntity.ok(invoice);
    }

    /*end point to update invoice status*/
    @PutMapping("/update-status")
    public ResponseEntity<Invoice> updateInvoiceStatus(@RequestBody InvoiceUpdateDTO invoiceUpdateDTO)
    {
        Invoice invoice = service.updateInvoiceStatus(invoiceUpdateDTO.getP_invoice_id(),invoiceUpdateDTO.getP_invoice_status(),invoiceUpdateDTO.getP_amount_paid());
        return ResponseEntity.ok(invoice);
    }

    @DeleteMapping("/delete-invoice/{p_invoice_id}")
    public ResponseEntity<String> deleteInvoice(@PathVariable Long p_invoice_id)
    {
        service.deleteInvoice(p_invoice_id);
        return ResponseEntity.status(200).body("Invoice Deleted Successfully");
    }

    @GetMapping("/invoices")
    public Page<Invoice> searchInvoices(
            @RequestParam int clientId,
            @RequestParam List<InvoiceStatus> statuses,
            @RequestParam(required = false) Long invoiceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return service.searchInvoices(clientId, statuses, invoiceId, page, size);
    }

}
