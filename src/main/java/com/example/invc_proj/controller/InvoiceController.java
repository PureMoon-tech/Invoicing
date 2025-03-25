package com.example.invc_proj.controller;

import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.model.ServicesRequested;
import com.example.invc_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private ProductService service;

    @GetMapping("/servicesRequested")
    public List<ServicesRequested> getAllServicesRequested()
    {
        return service.getAllServicesRequested();
    }

    @GetMapping("/servicesRequested/{invoice_id}")
    public Optional<ServicesRequested> getAllServicesRequested(@PathVariable int invoice_id)
    {
        System.out.println("calling getServicesRequestedByInvoice");
        return service.getServicesRequestedByInvoice(invoice_id);
    }


    @PostMapping("/servicesRequested")
    public void addServiceTOInvoice(@RequestBody ServicesRequested invc_srvc)
    {
        service.addServiceTOInvoice(invc_srvc);
    }


    @GetMapping("/invoices")
    public List<Invoice> getAllInvoices()
    {
        return service.getAllInvoices();
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
