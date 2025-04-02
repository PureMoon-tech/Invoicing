package com.example.invc_proj.controller;

import com.example.invc_proj.dto.ServiceCostRequest;
import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.model.ServicesRequested;
import com.example.invc_proj.service.InvoiceService;
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

    @Autowired
    private InvoiceService service2;

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

    @PostMapping("/generate_Invoice/{p_client_Id}/{p_bank_id}/{p_invoice_status}")
    public Invoice getInvoice (@PathVariable Integer p_client_Id,
                               @PathVariable Integer p_bank_id,
                               @PathVariable String p_invoice_status,
                               @RequestBody List<ServiceCostRequest> serviceCostRequest)
    {
        System.out.println(p_client_Id);
        System.out.println(serviceCostRequest);
        return service2.generateInvoice(p_client_Id,p_bank_id,p_invoice_status,serviceCostRequest);
    }

   /* @PatchMapping("/update_Status/{p_invoice_id}/{p_invoice_status}")
    public Invoice updateInvoiceStatus(@PathVariable Integer p_invoice_id,
                                       @PathVariable String p_invoice_status)
    {
        System.out.println(p_invoice_id);
        System.out.println(p_invoice_status);
        return service2.updateInvoiceStatus(p_invoice_id,p_invoice_status);
    }*/

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
