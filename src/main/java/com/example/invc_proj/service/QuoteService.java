package com.example.invc_proj.service;

import com.example.invc_proj.dto.ServiceCostRequest;
import com.example.invc_proj.exceptions.NotFoundException;
import com.example.invc_proj.model.*;
import com.example.invc_proj.model.Enum.QuoteStatus;
import com.example.invc_proj.model.Enum.QuoteType;
import com.example.invc_proj.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class QuoteService {


    private final ClientRepo clientRepository;
    private final ServicesRepo serviceRepository;
    private final QuoteRepo quoteRepository;
    private final ServicesQuotedRepo servicesQuotedRepository;
    private final UserService userService;
    private final QuoteNumGeneratorService quoteNumGeneratorService;


    public Quote generateQuote(int clientId,QuoteStatus quoteStatus, QuoteType quoteType, List<ServiceCostRequest> serviceCosts) {

        // Get the currently authenticated user's username
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userPrincipal.getUsername();

        Optional<User> user = userService.getUserByName(username);
        int userId = user.get().getId();

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found"));

        BigDecimal totalAmount = BigDecimal.ZERO;

        List<ServicesQuoted> servicesQuotedList = new ArrayList<>();

        for (ServiceCostRequest serviceCostRequest : serviceCosts) {
            Services service = serviceRepository.findById(serviceCostRequest.getServiceId())
                    .orElseThrow(() -> new NotFoundException("Service not found"));

            ServicesQuoted servicesQuoted = new ServicesQuoted();

            servicesQuoted.setService_id(service.getService_id());
            servicesQuoted.setClient_id(clientId);
            servicesQuoted.setService_cost(serviceCostRequest.getServiceCost());
            servicesQuoted.setTds_rate(calculateTdsRate(service));  // Method to calculate TDS rate
            servicesQuoted.setGst_rate(calculateGstRate(service));  // Method to calculate GST rate
            servicesQuoted.setService_total(calculateServiceTotal(servicesQuoted));  // Calculate the total for this service

            servicesQuoted.setUser_id(userId); // Set the logged-in user's ID
            servicesQuoted.setLast_update_date(new Date());
            servicesQuotedList.add(servicesQuoted);

            // Add to the total amount
            totalAmount = totalAmount.add(servicesQuoted.getService_total());
        }

        Quote quote = new Quote();
        quote.setClient_id(clientId);
        quote.setTotal(totalAmount);

        quote.setQuote_generated_date(new Date());
        quote.setLast_updated_date(new Date());
        quote.setUser_id(userId); // Set the logged-in user's ID
        quote.setQuote_number(quoteNumGeneratorService.generateQuoteNumber(quoteType));
        quote.setStatus(quoteStatus);
        System.out.println(quote);

        Quote savedQuote = quoteRepository.save(quote);

        System.out.print(savedQuote);

        for (ServicesQuoted serviceRequest : servicesQuotedList) {
            serviceRequest.setQuote_id(savedQuote);
        }

        servicesQuotedRepository.saveAll(servicesQuotedList);
        return savedQuote;
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
    private BigDecimal calculateServiceTotal(ServicesQuoted servicesQuoted) {
        BigDecimal serviceCost = servicesQuoted.getService_cost();
        int gstRate = servicesQuoted.getGst_rate();
        int tdsRate = servicesQuoted.getTds_rate();
        BigDecimal gstValue = calculateGST(serviceCost,gstRate);
        BigDecimal tdsValue = calculateTDS(serviceCost,tdsRate);
        servicesQuoted.setGst_value(gstValue);
        servicesQuoted.setTds_value(tdsValue);
        return serviceCost
                .add(gstValue)
                .add(tdsValue)
                .setScale(2, RoundingMode.HALF_UP);

    }
    private BigDecimal calculateTotalCost;



    public BigDecimal calculateGST(BigDecimal total, int gstRate) {
        BigDecimal rate = BigDecimal.valueOf(gstRate).divide(BigDecimal.valueOf(100)); // Converts gstRate to a percentage
        BigDecimal gstValue = total.multiply(rate).setScale(2, RoundingMode.HALF_UP); // Calculates GST with rounding
        return gstValue;
    }


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


    public List<Quote> getAllQuotes(QuoteStatus quote_status)
    {
        // Fetch all quotes from the repository
        // Filter out only the active quotes
        // If no active quotes are found, throw an exception
        // Otherwise, return the list of active quotes

     List<Quote> quotes = quoteRepository.findAllByStatus(quote_status);
     /*List<Quote> activeQuotes = quotes.stream()
                .filter(quote -> quote.getStatus().equalsIgnoreCase(String.valueOf(quote_status)))
                .toList();*/

     if(quotes.isEmpty())
     {
         throw new NotFoundException("No Quotes Found");
     }
     else return quotes;
    }
}