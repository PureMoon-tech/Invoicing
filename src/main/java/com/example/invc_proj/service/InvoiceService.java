package com.example.invc_proj.service;

import com.example.invc_proj.dto.InvoiceResponseDTO;
import com.example.invc_proj.dto.InvoiceResponseDTOOld;
import com.example.invc_proj.dto.InvoiceView;
import com.example.invc_proj.dto.ServiceCostRequest;
import com.example.invc_proj.mapper.InvoiceResponseDTOMapper;
import com.example.invc_proj.mapper.InvoiceResponseDTOOldMapper;
import com.example.invc_proj.model.*;
import com.example.invc_proj.model.Enum.InvoiceStatus;
import com.example.invc_proj.model.Enum.InvoiceType;
import com.example.invc_proj.repository.*;
import com.example.invc_proj.service.Event.InvoiceGeneratedEvent;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class InvoiceService {

    private final ClientRepo clientRepository;
    private final ServicesRepo serviceRepository;
    private final InvoiceRepo invoiceRepository;
    private final ServicesRequestedRepo servicesRequestedRepository;
    private final UserService userService;
    private final InvoiceNumGeneratorService invoiceNumGeneratorService;
    private final ApplicationEventPublisher eventPublisher;
    private final InvoiceViewRepo invoiceViewRepo;

    public InvoiceResponseDTO generateInvoice(int clientId, int bankId , InvoiceStatus invoiceStatus, InvoiceType invoiceType, BigDecimal amountPaid, List<ServiceCostRequest> serviceCosts) {

        //String username = ((USERS) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserName();
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userPrincipal.getUsername();
        System.out.println(username);
        Optional<User> user = userService.getUserByName(username);
        int userId = user.get().getId();
        //int total = serviceCosts.getFirst().getTotalCost();

        Client client = clientRepository.findById(clientId)
                                        .orElseThrow(() -> new RuntimeException("Client not found"));

        BigDecimal totalAmount = BigDecimal.ZERO;

        List<ServicesRequested> servicesRequestedList = new ArrayList<>();

        // Loop through each service cost entry to create the services requested
        for (ServiceCostRequest serviceCostRequest : serviceCosts) {
            // Fetch the selected service from the database using the serviceId
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
        invoice.setTotal(totalAmount); // Setting the total amount as an integer (you can choose to keep it BigDecimal as well)
        invoice.setAmountPaid(amountPaid);
        //invoice.setStatus(invoiceStatus);  // Default status
        invoice.setStatus(validateInvoiceStatus(null,totalAmount,amountPaid,invoiceStatus));
        invoice.setInvoice_generated_date(new Date());
        invoice.setLast_updated_date(new Date());
        invoice.setUser_id(userId); // Set the logged-in user's ID
        invoice.setBank_id(bankId);
        invoice.setInvoice_number(invoiceNumGeneratorService.generateInvoiceNumber(invoiceType));

        System.out.println(invoice);
        // Save the invoice to the repository
        Invoice savedInvoice = invoiceRepository.save(invoice);

        System.out.print(savedInvoice);

        // Now that the invoice is saved, updating the ServicesRequested list with the correct invoiceId
        //servicesRequestedList.forEach(serviceRequest -> serviceRequest.setInvoice_id(savedInvoice.getInvoice_id()));
        for (ServicesRequested serviceRequest : servicesRequestedList) {
            serviceRequest.setInvoice_id(savedInvoice); // Using the Invoice object directly
        }
        // Save all the services requested for this invoice
        servicesRequestedRepository.saveAll(servicesRequestedList);
        invoiceRepository.flush();
        //receipt generation event publishing
        eventPublisher.publishEvent(new InvoiceGeneratedEvent(savedInvoice,amountPaid));
        InvoiceResponseDTO invoiceResponseDTO = InvoiceResponseDTOMapper.toDTO(savedInvoice);
        return invoiceResponseDTO;
    }

    //Method to calculate TDS rate
    private int calculateTdsRate(Services service) {
        // TDS rate might depend on the service type
        return 2; // Assuming a fixed TDS rate for simplicity
    }

    // Method to calculate GST rate
    private int calculateGstRate(Services service) {
        // GST might depend on service type, etc.
        return 18; // Assuming a fixed GST rate for simplicity else get gst rate according to the service
    }

    // Calculate the total amount for the service (including GST, TDS, etc.)
    private BigDecimal calculateServiceTotal(ServicesRequested servicesRequested) {
        BigDecimal serviceCost = servicesRequested.getService_cost();
        int gstRate = servicesRequested.getGst_rate();
        int tdsRate = servicesRequested.getTds_rate();
        BigDecimal gstValue = calculateGST(serviceCost,gstRate);
        BigDecimal tdsValue = calculateTDS(serviceCost,tdsRate);
        servicesRequested.setGst_value(gstValue);
        servicesRequested.setTds_value(tdsValue);
        return serviceCost
                .add(gstValue)
                .add(tdsValue)
                .setScale(2, RoundingMode.HALF_UP);
        //int tdsValue = serviceCost.multiply(BigDecimal.valueOf(tdsRate)).divide(BigDecimal.valueOf(100));
        //double tdsValue = calculateTDS(total,tdsRate);
        // Calculate the total cost for the service (including GST, subtracting TDS)
        // int tdsRate = servicesRequested.getTds_rate();
        //int gstValue = serviceCost.multiply(BigDecimal.valueOf(gstRate)).divide(BigDecimal.valueOf(100));
        //return BigDecimal.valueOf(serviceCost+gstValue);


    }
     private BigDecimal calculateTotalCost;


   /* public double calculateGST(double total, int gstRate)
    {
        double gst_value =0;
        gst_value = (total * gstRate/100) ;
        //gst_value = total + gst_value; if grand total is being calulated
        return gst_value;
    }*/
   public BigDecimal calculateGST(BigDecimal total, int gstRate) {
       BigDecimal rate = BigDecimal.valueOf(gstRate).divide(BigDecimal.valueOf(100)); // Converts gstRate to a percentage
       BigDecimal gstValue = total.multiply(rate).setScale(2, RoundingMode.HALF_UP); // Calculates GST with rounding
       return gstValue;
   }


  /*  public double calculateTDS( double total, int tdsRate)
    {
        double tds_value=0;
        tds_value = (total * tdsRate/100) ;
        //gst_value = total + gst_value; if grand total is being calulated
        return tds_value;

    }
   */
    public BigDecimal calculateTDS( BigDecimal total, int tdsRate)
    {
        BigDecimal rate = BigDecimal.valueOf(tdsRate).divide(BigDecimal.valueOf(100)); // Converts gstRate to a percentage
        BigDecimal tdsValue = total.multiply(rate).setScale(2, RoundingMode.HALF_UP); // Calculates GST with rounding
        return tdsValue;
    }

    public double calculateTotalCost( double total, double gstValue, double tdsValue)
    {
       return total+gstValue-tdsValue;
    }

    public Invoice updateInvoiceStatus(Long pInvoiceId, InvoiceStatus pInvoiceStatus,BigDecimal pAmountPaid)
    {
        Invoice invoice = invoiceRepository.findById(pInvoiceId).
                orElseThrow(()-> new RuntimeException("No invoice found for the invoice number"));
        //invoice.setStatus(pInvoiceStatus);
        BigDecimal total = invoice.getTotal();
        invoice.setAmountPaid(updateAmountPaid(pInvoiceId,pAmountPaid));
        BigDecimal updatedAmountPaid = invoice.getAmountPaid();
        invoice.setStatus(validateInvoiceStatus(pInvoiceId,total,updatedAmountPaid,pInvoiceStatus));
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return updatedInvoice;

    }

    private BigDecimal updateAmountPaid(Long pInvoiceId, BigDecimal pAmountPaid)
    {
        Invoice invoice = invoiceRepository.findById(pInvoiceId).
                orElseThrow(()-> new RuntimeException("No invoice found for the invoice number"));
        return pAmountPaid.add(invoice.getAmountPaid());
    }

    public void deleteInvoice(Long pInvoiceId) {
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
           invoiceRepository.delete(invoice);

    }

    public InvoiceStatus validateInvoiceStatus(Long InvoiceId,BigDecimal TotalAmount, BigDecimal AmountPaid, InvoiceStatus status)
    {
        if (InvoiceId==null)
        {
           int result = TotalAmount.compareTo(AmountPaid);

            if (result==0)
            {
                return InvoiceStatus.PAID;
            }
            else if(result>0)
            {
                return InvoiceStatus.PARTIALLY_PAID;
            }
            else
            {
                return InvoiceStatus.UNPAID;
            }
        }
        else
        {
            Optional<Invoice> invc = invoiceRepository.findById(InvoiceId);
            BigDecimal PaidAmount = invc.get().getAmountPaid();
            int result =  TotalAmount.compareTo(AmountPaid.add(PaidAmount));
            if (result==0)
            {
                return InvoiceStatus.PAID;
            }
            else if(result>0)
            {
                BigDecimal amount = invc.get().getTotal();
                BigDecimal amountPaid = AmountPaid.add(PaidAmount);
                return InvoiceStatus.PARTIALLY_PAID;
            }
        }
        return status;
    }

    public  List<InvoiceResponseDTOOld> getAllInvoices()
    {
        List<InvoiceResponseDTOOld> invoiceResponseDTO = InvoiceResponseDTOOldMapper.toDTOList(invoiceRepository.findAll());
        return invoiceResponseDTO;
    }

    public  List<InvoiceView> getAllInvoice()
    {
        List<InvoiceView> invoiceView = invoiceViewRepo.findAll();
        return invoiceView;
    }

    public Page<Invoice> searchInvoices(int clientId,
                                        List<InvoiceStatus> statuses,
                                        Long invoiceId,
                                        int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return invoiceRepository.findByClientIdAndStatusesAndOptionalInvoiceId(clientId, statuses, invoiceId, pageable);
    }


}