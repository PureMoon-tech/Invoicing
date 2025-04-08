package com.example.invc_proj.service;

import com.example.invc_proj.dto.ServiceCostRequest;
import com.example.invc_proj.model.*;
import com.example.invc_proj.repository.ClientRepo;
import com.example.invc_proj.repository.InvoiceRepo;
import com.example.invc_proj.repository.ServicesRepo;
import com.example.invc_proj.repository.ServicesRequestedRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InvoiceService {

    @Autowired
    private ClientRepo clientRepository;

    @Autowired
    private ServicesRepo serviceRepository;

    @Autowired
    private InvoiceRepo invoiceRepository;

    @Autowired
    private ServicesRequestedRepo servicesRequestedRepository;
    @Autowired
    private UserService userService; // Assuming this service provides user info by username

    public Invoice generateInvoice(int clientId, int bankId , String invoiceStatus,List<ServiceCostRequest> serviceCosts) {
        // Get the currently authenticated user's username
        System.out.println(serviceCosts);
        //String username = ((USERS) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserName();
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
// Then, you can access the user information from UserPrincipal and map it to your USERS class if needed
// Assuming 'UserPrincipal' has a 'getUsername' method
        String username = userPrincipal.getUsername();

        // Fetch the user object by username
        Optional<User> user = userService.getUserByName(username);  // Assuming this method returns a User entity with user_id

        int userId = user.get().getId();  // Assuming `getUserId()` returns the user ID
        int total = serviceCosts.getFirst().getTotalCost();

        // Find the client by clientId (you can throw exception if client is not found)
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found"));

        // Initialize total amount and tax calculations
        BigDecimal totalAmount = BigDecimal.ZERO;

        List<ServicesRequested> servicesRequestedList = new ArrayList<>();

        // Loop through each service cost entry to create the services requested
        for (ServiceCostRequest serviceCostRequest : serviceCosts) {
            // Fetch the selected service from the database using the serviceId to get min and max prices
            Services service = serviceRepository.findById(serviceCostRequest.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));

            // Create the ServicesRequested entity and add it to the list
            ServicesRequested servicesRequested = new ServicesRequested();

            servicesRequested.setService_id(service.getService_id());
            servicesRequested.setClient_id(clientId);
           // servicesRequested.setServiceName(serviceCostRequest.getServiceName());  // Use the service name from the DTO
            servicesRequested.setService_cost(serviceCostRequest.getServiceCost());
            // You can add further calculations for TDS, GST, etc., here
            servicesRequested.setTds_rate(calculateTdsRate(service));  // Assuming a method to calculate TDS rate
            servicesRequested.setGst_rate(calculateGstRate(service));  // Assuming a method to calculate GST rate
            servicesRequested.setService_total(calculateServiceTotal(servicesRequested));  // Calculate the total for this service

            // servicesRequested.setInvoice_id(0); // We'll set the correct invoice ID later
            servicesRequested.setUser_id(userId); // Set the logged-in user's ID
            servicesRequested.setLast_update_date(new Date());

            // Add to the services list for the invoice
            servicesRequestedList.add(servicesRequested);

            // Add to the total amount
            totalAmount = totalAmount.add(servicesRequested.getService_total());
        }

        // Create the Invoice object
        Invoice invoice = new Invoice();
        invoice.setClient_id(clientId);
        invoice.setTotal(totalAmount.intValue()); // Setting the total amount as an integer (you can choose to keep it BigDecimal as well)
        invoice.setStatus(invoiceStatus);  // Default status
        invoice.setInvoice_generated_date(new Date());
        invoice.setLast_updated_date(new Date());
        invoice.setUser_id(userId); // Set the logged-in user's ID
        invoice.setBank_id(bankId);

        System.out.println(invoice);
        // Save the invoice to the repository
        Invoice savedInvoice = invoiceRepository.save(invoice);

        System.out.print(savedInvoice);

        // Now that the invoice is saved, update the ServicesRequested list with the correct invoiceId
        //servicesRequestedList.forEach(serviceRequest -> serviceRequest.setInvoice_id(savedInvoice.getInvoice_id()));
        for (ServicesRequested serviceRequest : servicesRequestedList) {
            serviceRequest.setInvoice_id(savedInvoice); // Using the Invoice object directly
        }
        // Save all the services requested for this invoice
        servicesRequestedRepository.saveAll(servicesRequestedList);



        return savedInvoice;
    }

    // Example method to calculate TDS rate (implement based on your business logic)
    private int calculateTdsRate(Services service) {
        // For example, TDS rate might depend on the service type or other factors
        return 10; // Assuming a fixed TDS rate for simplicity
    }

    // Example method to calculate GST rate (implement based on your business logic)
    private int calculateGstRate(Services service) {
        // Similar to TDS, GST might depend on service type, etc.
        return 18; // Assuming a fixed GST rate for simplicity
    }

    // Calculate the total amount for the service (including GST, TDS, etc.)
    private BigDecimal calculateServiceTotal(ServicesRequested servicesRequested) {
        int serviceCost = servicesRequested.getService_cost();
        int gstRate = servicesRequested.getGst_rate();
        double gstValue = calculateGST(serviceCost,gstRate);
        servicesRequested.setGst_value((int)(gstValue));
        //int tdsValue = serviceCost.multiply(BigDecimal.valueOf(tdsRate)).divide(BigDecimal.valueOf(100));
        //double tdsValue = calculateTDS(total,tdsRate);
        // Calculate the total cost for the service (including GST, subtracting TDS)
        // int tdsRate = servicesRequested.getTds_rate();
        //int gstValue = serviceCost.multiply(BigDecimal.valueOf(gstRate)).divide(BigDecimal.valueOf(100));
        return BigDecimal.valueOf(serviceCost+gstValue);
    }
     private BigDecimal calculateTotalCost;


    public double calculateGST(double total, int gstRate)
    {
        double gst_value =0;
        gst_value = (total * gstRate/100) ;
        //gst_value = total + gst_value; if grand total is being calulated
        return gst_value;
    }

    public double calculateTDS( double total, int tdsRate)
    {
        double tds_value=0;
        tds_value = (total * tdsRate/100) ;
        //gst_value = total + gst_value; if grand total is being calulated
        return tds_value;
    }

    public double calculateTotalCost( double total, double gstValue, double tdsValue)
    {
       return total+gstValue-tdsValue;
    }

    public Invoice updateInvoiceStatus(Integer pInvoiceId, String pInvoiceStatus)
    {
        Invoice invoice = invoiceRepository.findById(pInvoiceId).
                orElseThrow(()-> new RuntimeException("Service not found"));
        invoice.setStatus(pInvoiceStatus);
        Invoice updatedInvoice = invoiceRepository.save(invoice);

        return updatedInvoice;

    }

    public void deleteInvoice(Integer pInvoiceId) {
        List<ServicesRequested> servicesRequested = servicesRequestedRepository.findByInvoiceId(pInvoiceId);

          if (servicesRequested == null)
          {
              throw new RuntimeException("No Services found for the Invoice");
          }

        for(ServicesRequested srvc : servicesRequested )
        {
            servicesRequestedRepository.deleteById(srvc.getRequest_id());
        }


         Invoice invoice =  invoiceRepository.findById(pInvoiceId).
                  orElseThrow(()-> new RuntimeException("No invoice found for the invoice number"));


    }
}