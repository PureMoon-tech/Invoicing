package com.example.invc_proj.controller;

import com.example.invc_proj.dto.ServiceCostRequest;
import com.example.invc_proj.model.Enum.InvoiceStatus;
import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.model.Enum.InvoiceType;
import com.example.invc_proj.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
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
        System.out.println(p_client_Id);
        System.out.println(serviceCostRequest);
        Invoice invoice = service.generateInvoice(p_client_Id,p_bank_id,p_invoice_status,p_invoice_type,p_amount_paid,serviceCostRequest);
        return ResponseEntity.status(201).body(invoice);
    }

    /*end point to update invoice status*/
    @PutMapping("/update-status/{p_invoice_id}/{p_invoice_status}/{p_amount_paid}")
    public ResponseEntity<Invoice> updateInvoiceStatus(@PathVariable Long p_invoice_id,
                                       @PathVariable InvoiceStatus p_invoice_status,
                                       @PathVariable BigDecimal p_amount_paid)
    {
        System.out.println(p_invoice_id);
        System.out.println(p_invoice_status);
         Invoice invoice = service.updateInvoiceStatus(p_invoice_id,p_invoice_status,p_amount_paid);
          return ResponseEntity.ok(invoice);
    }

    @DeleteMapping("/delete-invoice/{p_invoice_id}")
    public ResponseEntity<String> deleteInvoice(@PathVariable Long p_invoice_id)
    {
        service.deleteInvoice(p_invoice_id);
        return ResponseEntity.status(200).body("Invoice Deleted Successfully");
    }


}
