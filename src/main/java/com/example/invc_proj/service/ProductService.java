package com.example.invc_proj.service;

import com.example.invc_proj.dto.ServicesDTO;
import com.example.invc_proj.mapper.ServicesDTOMapper;
import com.example.invc_proj.model.*;
import com.example.invc_proj.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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


    public  List<Invoice> getAllInvoices() { return repository_invc.findAll(); }

    public  List<ServicesRequested> getAllServicesRequested()
            { return repository_invc_srvc.findAll(); }


    public Optional<Services> getServiceById(int id)
    {
      System.out.println("get by id"+id);
       return Optional.ofNullable(repository_srvc.findById(id).orElseThrow(() -> new RuntimeException("Service not found")));
    }


    public List<ServicesRequested> getServicesRequestedByInvoice(Long invoiceId)
    {
      System.out.println("get by id"+invoiceId);
      List<ServicesRequested> servicesRequested = repository_invc_srvc.findByInvoiceId(invoiceId);
        //repository_invc_srvc.findById(invoiceId)
      return servicesRequested;
    }


  public void addServices(ServicesDTO srvc)
  {
     Services services = ServicesDTOMapper.mapToService(srvc);
    repository_srvc.save(services);
  }

  public void  CreateInvoice(Invoice invc)  { repository_invc.save(invc); }

  public void  addServiceTOInvoice(ServicesRequested invc_srvc)
  {

    repository_invc_srvc.save(invc_srvc);

  }

 public Integer getInvoiceId(String seq_invoice_id)
  {

      return SeqNextVal.getNextSequenceValue(seq_invoice_id);
  }


  /*get current financial year total in order to process tds*/
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
   //tds_value = total - tds_value; if grand total is being calulated
    return tds_value;
 }


    public void updateServiceStatus(int pServiceId)
    {
       Services services = repository_srvc.findById(pServiceId).
               orElseThrow(() -> new RuntimeException("Service not found"));
    }

    public void deleteService(int pServiceId)
    {
      repository_srvc.deleteById(pServiceId);
    }
}





/*
  @Autowired
  private UserRepo repository_user;
  public Optional<Login> getUserCredentials(int userId)
  {
    return repository_user.findById(userId);

  }*/