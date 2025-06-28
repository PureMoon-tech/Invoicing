package com.example.invc_proj.controller;

import com.example.invc_proj.dto.ServiceCostRequest;
import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.model.InvoiceType;
import com.example.invc_proj.model.ServicesRequested;
import com.example.invc_proj.service.InvoiceService;
import com.example.invc_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/invoice")
@PreAuthorize("isAuthenticated()")
public class InvoiceController {

    @Autowired
    private InvoiceService service;

    /*End point to generate invoice*/
    @PostMapping("/generate-invoice/{p_client_Id}/{p_bank_id}/{p_invoice_status}/{p_invoice_type}")
    public ResponseEntity<Invoice> generateInvoice (@PathVariable Integer p_client_Id,
                               @PathVariable Integer p_bank_id,
                               @PathVariable String p_invoice_status,
                               @PathVariable InvoiceType p_invoice_type,
                               @RequestBody List<ServiceCostRequest> serviceCostRequest)
    {
        System.out.println(p_client_Id);
        System.out.println(serviceCostRequest);
        Invoice invoice = service.generateInvoice(p_client_Id,p_bank_id,p_invoice_status,p_invoice_type,serviceCostRequest);
        return ResponseEntity.status(201).body(invoice);
    }

    /*end point to update invoice status*/
    @PutMapping("/update-status/{p_invoice_id}/{p_invoice_status}")
    public ResponseEntity<Invoice> updateInvoiceStatus(@PathVariable Integer p_invoice_id,
                                       @PathVariable String p_invoice_status)
    {
        System.out.println(p_invoice_id);
        System.out.println(p_invoice_status);
         Invoice invoice = service.updateInvoiceStatus(p_invoice_id,p_invoice_status);
          return ResponseEntity.ok(invoice);
    }

    @DeleteMapping("/delete-invoice/{p_invoice_id}")
    public ResponseEntity<String> deleteInvoice(@PathVariable Integer p_invoice_id)
    {
        service.deleteInvoice(p_invoice_id);
        return ResponseEntity.status(200).body("Invoice Deleted Successfully");
    }


}
