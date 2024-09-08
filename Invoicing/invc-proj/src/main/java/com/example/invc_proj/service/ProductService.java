package com.example.invc_proj.service;

import com.example.invc_proj.model.*;
import com.example.invc_proj.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


  private final ServicesRepo repository_srvc;
  private final ClientRepo repository_clnt;
  private final BankRepo repository_bnk;
  private final InvoiceRepo repository_invc;
  private final ServicesRequestedRepo repository_invc_srvc;
 private final SequenceNextValRepo SeqNextVal;
 private final FyTotalRepo FyTotal;


  @Autowired
  public ProductService( ServicesRepo repository_srvc,
                        ClientRepo repository_clnt, BankRepo repository_bnk,
                        InvoiceRepo repository_invc,ServicesRequestedRepo repository_invc_srvc,
                         SequenceNextValRepo SeqNextVal,
                         FyTotalRepo  FyTotal
                         )
  {

    this.repository_srvc = repository_srvc;
    this.repository_clnt = repository_clnt;
    this.repository_bnk = repository_bnk;
    this.repository_invc = repository_invc;
    this.repository_invc_srvc = repository_invc_srvc;
    this.SeqNextVal = SeqNextVal;
    this.FyTotal = FyTotal;
  }



    public List<Services> getAllServices()
  {
    return repository_srvc.findAll();
  }

    public List<Client> getAllClients()
  {
    return repository_clnt.findAll();
  }

    public List<BankDetails> getAllBankDetails()
  {
    return repository_bnk.findAll();
  }

    public  List<Invoice> getAllInvoices() { return repository_invc.findAll(); }

    public  List<ServicesRequested> getAllServicesRequested()
            { return repository_invc_srvc.findAll(); }







    public Optional<Services> getServiceById(int id)
    {
      System.out.println("get by id"+id);
       return repository_srvc.findById(id);
    }


    public Optional<Client> getClientById(int id)
    {
      System.out.println("get by id"+id);
       return repository_clnt.findById(id);

    }
    public Optional<BankDetails> getBankDetailsById(int id)
    {
      System.out.println("get by id"+id);
      return repository_bnk.findById(id);
    }

    public Optional<ServicesRequested> getServicesRequestedByInvoice(int invoiceId)
    {
      System.out.println("get by id"+invoiceId);
      return repository_invc_srvc.findById(invoiceId);
    }



  public void addServices(Services srvc) {
    repository_srvc.save(srvc);
  }

  public void addClients(Client clnt)
  {
    repository_clnt.save(clnt);
  }

  public void addBankDetails(BankDetails bnk)
  {
    repository_bnk.save(bnk);
  }

  public void  CreateInvoice(Invoice invc)  { repository_invc.save(invc); }

  public void  addServiceTOInvoice(ServicesRequested invc_srvc)
  {

    repository_invc_srvc.save(invc_srvc);

  }

  public String Login(Login userLogin)
  {
    /*int user_id;
    if (userLogin.getUser_id()=0)
    {
      return "user_does_exists" ;

    }
    else
    {
      //user_id = Integer.parseInt(userLogin.getUser_id());
      return "user_doesn't exists: "+user_id;
    }
    */
    return "user_does_exists" ;
  }

 /* public Integer getInvoiceId()
  {
    return 1;
  }
*/

 public Integer getInvoiceId(String seq_invoice_id)
  {
    return SeqNextVal.getNextSequenceValue(seq_invoice_id);
  }



  public Integer getCurrentFYTotal(Integer clientId)
  {
      System.out.println("clientId: "+clientId);
    return FyTotal.get_current_fy_total(clientId);
  }


  public double calculateGST(double total)
  {
    double gst_value =0;
        gst_value = (total * 0.18) ;
        //gst_value = total + gst_value; if grand total is being calulated
    return gst_value;
  }

 public double calculateTDS( double total)
 {
    double tds_value=0;
    tds_value = (total * 0.10) ;
   //gst_value = total + gst_value; if grand total is being calulated
    return tds_value;
 }





}





/*
  @Autowired
  private UserRepo repository_user;
  public Optional<Login> getUserCredentials(int userId)
  {
    return repository_user.findById(userId);

  }*/