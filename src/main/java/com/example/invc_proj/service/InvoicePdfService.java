package com.example.invc_proj.service;

import com.example.invc_proj.dto.InvoiceTotals;
import com.example.invc_proj.model.*;
import com.example.invc_proj.repository.BankRepo;
import com.example.invc_proj.repository.ClientRepo;
import com.example.invc_proj.repository.ServicesRepo;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoicePdfService {

    @Autowired
    private ClientRepo clientRepository;

    @Autowired
    private BankRepo bankRepository;

    @Autowired
    private ServicesRepo servicesRespository;

    public byte[] generateInvoicePdf(Invoice invoice, List<ServicesRequested> services) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add Invoice Title
            document.add(new Paragraph("Invoice")
                    .setFontSize(20)
                    .setMarginBottom(10));

            // Add Invoice Details
            document.add(new Paragraph("Invoice ID: " + invoice.getInvoice_id()));


            Client client = clientRepository.findById(invoice.getClient_id()).
                    orElseThrow(()-> new RuntimeException("Client Not Fuund"));
            BankDetails bankDetails = bankRepository.getReferenceById(invoice.getBank_id());

            document.add(new Paragraph("Client Name: " + client.getClient_name()));

            document.add(new Paragraph("Client Address: " +client.getAddress() ));

            // Table for Services
            Table table = new Table(6);
            table.addCell("No");
            table.addCell("Service Name");
            table.addCell("Cost");
            table.addCell("GST Rate");
            table.addCell("GST Value");
            table.addCell("Service Total");


            AtomicInteger count = new AtomicInteger(1);


            List<Integer> serviceIds = services.stream()
                    .map(ServicesRequested::getService_id)
                    .collect(Collectors.toList());

            Map<Integer, Services> serviceMap = servicesRespository.findAllById(serviceIds)
                    .stream()
                    .collect(Collectors.toMap(Services::getService_id, s -> s));

            for (ServicesRequested service : services) {

                //Services srvc = servicesRespository.findById(service.getService_id()).orElseThrow(()-> new RuntimeException("Services Not Found"));
                table.addCell(String.valueOf(count.getAndIncrement()));
                //table.addCell(String.valueOf(srvc.getService_name()));
                table.addCell(String.valueOf(serviceMap.get(service.getService_id()).getService_name()));
                table.addCell(String.valueOf(service.getService_cost()));
                table.addCell(String.valueOf(service.getGst_rate()));
                table.addCell(String.valueOf(service.getGst_value()));
                table.addCell(String.valueOf(service.getService_total()));
            }

            document.add(table);

            // Initialize totals
            BigDecimal totalServiceCost = BigDecimal.ZERO;
            BigDecimal totalGstValue = BigDecimal.ZERO;
            BigDecimal totalTdsValue = BigDecimal.ZERO;

            // Sum up the values for all services
            for (ServicesRequested service : services) {
                totalServiceCost = totalServiceCost.add(service.getService_cost());
                //totalGstValue += service.getGst_value();
                //totalTdsValue += service.getTds_value();
                totalGstValue = totalGstValue.add(service.getGst_value());
                totalTdsValue = totalTdsValue.add(service.getTds_value());

            }

            // Display the total values in the invoice
            document.add(new Paragraph("Total Service Cost: ₹" + totalServiceCost));
            document.add(new Paragraph("GST Value (Total): ₹" + totalGstValue));
            document.add(new Paragraph("TDS Value (Total): ₹" + totalTdsValue));
            document.add(new Paragraph("Grand Total Amount: $" + invoice.getTotal()));
            document.add(new Paragraph("Status: " + invoice.getStatus()));


            document.add(new Paragraph("Bank Account Details"));
            document.add(new Paragraph("ACCOUNT HOLDER NAME: "+ bankDetails.getAccount_holder_name()));
            document.add(new Paragraph("BANK NAME: "+ bankDetails.getBank_name()));
            document.add(new Paragraph("BRANCH: "+ bankDetails.getBranch()));
            document.add(new Paragraph("Account No: "+ bankDetails.getAccount_no()));
            document.add(new Paragraph("IFSC CODE: "+ bankDetails.getIfsc_code()));
            document.add(new Paragraph("MICR CODE: "+ bankDetails.getMicr_code()));
            document.add(new Paragraph("UPI ID: "+ bankDetails.getUpi_number()));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] generateInvoicePdf(Invoice invoice, List<ServicesRequested> services, Client client, BankDetails bankDetails, Map<Integer, Services> serviceMap)
    {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add Invoice Title
            document.add(new Paragraph("Invoice")
                    .setFontSize(20)
                    .setMarginBottom(10));

            // Add Invoice Details
            document.add(new Paragraph("Invoice ID: " + invoice.getInvoice_id()));
            
            document.add(new Paragraph("Client Name: " + client.getClient_name()));

            document.add(new Paragraph("Client Address: " +client.getAddress() ));

            // Table for Services
            Table table = new Table(6);
            table.addCell("No");
            table.addCell("Service Name");
            table.addCell("Cost");
            table.addCell("GST Rate");
            table.addCell("GST Value");
            table.addCell("Service Total");


            AtomicInteger count = new AtomicInteger(1);




            for (ServicesRequested service : services) {

                //Services srvc = servicesRespository.findById(service.getService_id()).orElseThrow(()-> new RuntimeException("Services Not Found"));
                table.addCell(String.valueOf(count.getAndIncrement()));
                //table.addCell(String.valueOf(srvc.getService_name()));
                table.addCell(String.valueOf(serviceMap.get(service.getService_id()).getService_name()));
                table.addCell(String.valueOf(service.getService_cost()));
                table.addCell(String.valueOf(service.getGst_rate()));
                table.addCell(String.valueOf(service.getGst_value()));
                table.addCell(String.valueOf(service.getService_total()));
            }

            document.add(table);

            // Initialize totals
            BigDecimal totalServiceCost = BigDecimal.ZERO;
            BigDecimal totalGstValue = BigDecimal.ZERO;
            BigDecimal totalTdsValue = BigDecimal.ZERO;

            // Sum up the values for all services
            for (ServicesRequested service : services) {
                totalServiceCost = totalServiceCost.add(service.getService_cost());
                //totalGstValue += service.getGst_value();
                //totalTdsValue += service.getTds_value();
                totalGstValue = totalGstValue.add(service.getGst_value());
                totalTdsValue = totalTdsValue.add(service.getTds_value());

            }

            // Display the total values in the invoice
            document.add(new Paragraph("Total Service Cost: ₹" + totalServiceCost));
            document.add(new Paragraph("GST Value (Total): ₹" + totalGstValue));
            document.add(new Paragraph("TDS Value (Total): ₹" + totalTdsValue));
            document.add(new Paragraph("Grand Total Amount: $" + invoice.getTotal()));
            document.add(new Paragraph("Status: " + invoice.getStatus()));


            document.add(new Paragraph("Bank Account Details"));
            document.add(new Paragraph("ACCOUNT HOLDER NAME: "+ bankDetails.getAccount_holder_name()));
            document.add(new Paragraph("BANK NAME: "+ bankDetails.getBank_name()));
            document.add(new Paragraph("BRANCH: "+ bankDetails.getBranch()));
            document.add(new Paragraph("Account No: "+ bankDetails.getAccount_no()));
            document.add(new Paragraph("IFSC CODE: "+ bankDetails.getIfsc_code()));
            document.add(new Paragraph("MICR CODE: "+ bankDetails.getMicr_code()));
            document.add(new Paragraph("UPI ID: "+ bankDetails.getUpi_number()));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
