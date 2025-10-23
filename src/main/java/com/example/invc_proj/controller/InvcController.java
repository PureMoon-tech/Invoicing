package com.example.invc_proj.controller;

import com.example.invc_proj.exceptions.ApiResponse;
import com.example.invc_proj.exceptions.ApiResponses;
import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.model.ServicesRequested;
import com.example.invc_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InvcController {

     @Autowired
     private ProductService service;


    @GetMapping("/servicesRequested")
    public ResponseEntity<ApiResponse<List<ServicesRequested>>> getAllServicesRequested()
    {
        List<ServicesRequested> servicesRequested =  service.getAllServicesRequested();
        return  ApiResponses.ok(servicesRequested);
    }

    @GetMapping("/servicesRequested/{invoice_id}")
    public ResponseEntity<ApiResponse<List<ServicesRequested>>> getAllServicesRequested(@PathVariable Long invoice_id)
    {
        //System.out.println("calling getServicesRequestedByInvoice");
        List<ServicesRequested> servicesRequested =   service.getServicesRequestedByInvoice(invoice_id);
        return ApiResponses.ok(servicesRequested);
    }


    @PostMapping("/servicesRequested")
    public ResponseEntity<ApiResponse<String>> addServiceTOInvoice(@RequestBody ServicesRequested invc_srvc)
    {
        service.addServiceTOInvoice(invc_srvc);
        return  ApiResponses.created("Service added successfully");
    }


    @GetMapping("/invoices")
    public ResponseEntity<ApiResponse<List<Invoice>>> getAllInvoices()
    {

        List<Invoice> invoices = service.getAllInvoices();
        return ApiResponses.ok(invoices);
    }

    @PostMapping("/invoices")
    public ResponseEntity<ApiResponse<String>> createInvoice(@RequestBody Invoice invc)
    {
        service.CreateInvoice(invc);
        return  ApiResponses.created("Invoice created successfully");
    }


    @GetMapping("/invoice_id/{seq_invoice_id}")
    public Integer getInvoiceId(@PathVariable String seq_invoice_id)
    {
        //seq_invoice_id = "seq_invoice_id" ;
        return service.getInvoiceId(seq_invoice_id);
    }

    @GetMapping("/get_FyTotal/{p_client_Id}")
    public Integer getFyTotal(@PathVariable Integer p_client_Id)
    {
        return service.getCurrentFYTotal(p_client_Id);
    }

     /*@PostMapping("/processInvoice")
    public void processInvoice(Invc invc)
     {
         Prcs_invc.
     }
*/

/*@PostMapping("/GenerateInvoice")
    public void addServicesRequested(@RequestBody ServicesRequested invc)

    {
        System.out.println("calling post");
        System.out.println(invc);
        service.addServicesRequested(invc);
    }
*/

}
