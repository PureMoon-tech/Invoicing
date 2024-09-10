package com.example.invc_proj.controller;


import com.example.invc_proj.model.*;
import com.example.invc_proj.service.Process_Invoice;
import com.example.invc_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ServicesController {

    @Autowired
    private ProductService service;

    //private Process_Invoice Prcs_invc;

@RequestMapping("/")
    public String greet()
    {
        return "Welcome to Invoicing";
    }

@PostMapping("/login")
    public String Login(Login user_login)
    {
        System.out.println("calling Login");
        System.out.println(user_login);
        //return "calling Login2";
        return service.Login(user_login);
    }


@GetMapping("/services")
    public List<Services> getAllServices()
    {
        return service.getAllServices();
    }

@GetMapping("/services/{id}")
    public Optional<Services> getServiceById(@PathVariable int id)
    {
        System.out.println("calling getServiceById");
        return service.getServiceById(id);
    }

@PostMapping("/services")
    public void addServices( Services srvc)
    {
        System.out.println("calling post");
        System.out.println(srvc);
        service.addServices(srvc);
    }


@GetMapping("/clients")
    public List<Client> getAllClients()
    {
        return service.getAllClients();
    }

@GetMapping("/clients/{id}")
    public Optional<Client> getClientById(@PathVariable int id)
    {
        System.out.println("calling getClientById");
        return service.getClientById(id);
    }

@PostMapping("/client")
    public void addClients( Client clnt)
    {
        System.out.println("calling post");
        System.out.println(clnt);
        service.addClients(clnt);
    }

@GetMapping("/bankdetails")
public List<BankDetails> getAllBankDetails()
{
    return service.getAllBankDetails();
}


@GetMapping("/bankdetailsdropdown")
    public List<BankDetailsDropDown> getAllBankDetailsDD()
    {

        List<BankDetails> bankDetailsList= service.getAllBankDetails();; // Retrieve data from your database
         List<BankDetailsDropDown> BankDetailsDD = new ArrayList<>();

        for (BankDetails bankDetails : bankDetailsList) {
            BankDetailsDropDown simplified = new BankDetailsDropDown();
            simplified.setBn_id(bankDetails.getBn_id());
            simplified.setBank_name(bankDetails.getBank_name());
            simplified.setAccount_no(bankDetails.getAccount_no());
            BankDetailsDD.add(simplified);
        }

        return BankDetailsDD;


    }


@GetMapping("/bankdetails/{id}")
    public Optional<BankDetails> getBankDetailsById(@PathVariable int id)
    {
        System.out.println("calling getBankDetailsById");
        return service.getBankDetailsById(id);
    }

@PostMapping("/bankdetails")
    public void addClients( BankDetails bnk)
    {
        System.out.println("calling post");
        System.out.println(bnk);
        service.addBankDetails(bnk);
    }

/*@PostMapping("/GenerateInvoice")
    public void addServicesRequested(@RequestBody ServicesRequested invc)

    {
        System.out.println("calling post");
        System.out.println(invc);
        service.addServicesRequested(invc);
    }
*/


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
        public void addServiceTOInvoice( ServicesRequested invc_srvc)
        {
             service.addServiceTOInvoice(invc_srvc);
        }


@GetMapping("/invoices")
        public List<Invoice> getAllInvoices()
        {
            return service.getAllInvoices();
        }

@PostMapping("/invoices")
        public void createInvoice( Invoice invc)
        {
             service.CreateInvoice(invc);
        }

/*@PostMapping("/processInvoice")
    public void processInvoice(Invc invc)
     {
         Prcs_invc.
     }
*/

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

}
