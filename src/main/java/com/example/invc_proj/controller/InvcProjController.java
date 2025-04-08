package com.example.invc_proj.controller;

import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.model.ServicesRequested;
import com.example.invc_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InvcProjController {

     @Autowired
     private ProductService service;


    @GetMapping("/servicesRequested")
    public ResponseEntity<List<ServicesRequested>> getAllServicesRequested()
    {
        List<ServicesRequested> servicesRequested =  service.getAllServicesRequested();
        return  ResponseEntity.ok().body(servicesRequested);
    }

    @GetMapping("/servicesRequested/{invoice_id}")
    public ResponseEntity<List<ServicesRequested>> getAllServicesRequested(@PathVariable int invoice_id)
    {
        System.out.println("calling getServicesRequestedByInvoice");
        List<ServicesRequested> servicesRequested =   service.getServicesRequestedByInvoice(invoice_id);
        return ResponseEntity.ok().body(servicesRequested);
    }


    @PostMapping("/servicesRequested")
    public ResponseEntity<String> addServiceTOInvoice(@RequestBody ServicesRequested invc_srvc)
    {
        service.addServiceTOInvoice(invc_srvc);
        return  ResponseEntity.status(201).body("Service added successfully");
    }


    @GetMapping("/invoices")
    public ResponseEntity<List<Invoice>> getAllInvoices()
    {

        List<Invoice> invoices = service.getAllInvoices();
        return ResponseEntity.ok().body(invoices);
    }

    @PostMapping("/invoices")
    public void createInvoice(@RequestBody Invoice invc)
    {
        service.CreateInvoice(invc);
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
