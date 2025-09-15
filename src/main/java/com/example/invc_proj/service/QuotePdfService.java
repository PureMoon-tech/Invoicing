package com.example.invc_proj.service;

import com.example.invc_proj.model.*;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class QuotePdfService {


    public byte[] generateQuotePdf(Quote quote, List<ServicesQuoted> services,Map<Integer, Services> serviceMap)
    {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add Invoice Title
            document.add(new Paragraph("Quote")
                    .setFontSize(20)
                    .setMarginBottom(10));

            // Add Invoice Details
            document.add(new Paragraph("Quote ID: " + quote.getQuote_id()));

            // Table for Services
            Table table = new Table(6);
            table.addCell("No");
            table.addCell("Service Name");
            table.addCell("Cost");
            table.addCell("GST Rate");
            table.addCell("GST Value");
            table.addCell("Service Total");


            AtomicInteger count = new AtomicInteger(1);




            for (ServicesQuoted service : services) {

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
            for (ServicesQuoted service : services) {
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
            document.add(new Paragraph("Grand Total Amount: $" + quote.getTotal()));
           // document.add(new Paragraph("Status: " + quote.getStatus()));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
