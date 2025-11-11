package com.example.invc_proj.controller;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.example.invc_proj.model.Client;
import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.repository.ClientRepo;
import com.example.invc_proj.repository.UserRepo;
import com.example.invc_proj.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

@RestController
@RequestMapping("/api/preview")
public class BillController {

  /*  @Autowired
    ClientRepo clientRepository;

    private final InvoiceService invoiceService;

    public BillController(InvoiceService invoiceService)
    {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/invoice")
    public Map<String, String> generateBill(@RequestBody Invoice invoice) {
        Map<String, Float> calculatedValues = billService.calculateBill(request);
        Map<String, String> response = new HashMap<>();


        try {
            String filename = "Bill.pdf";
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(new FileOutputStream(filename)));
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("ANTHEA MAMMEN & CO").setBold().setFontSize(16));
            document.add(new Paragraph("Chartered Accountant\n\n"));
            document.add(new Paragraph("BILL\n").setBold().setFontSize(14));

            Client client = clientRepository.findById(invoice.getClient_id());


            document.add(new Paragraph("Client: " + request.getClientName()));
            document.add(new Paragraph("Date: " + request.getDate() + "\n\n"));

            Table table = new Table(2);
            table.addCell("Total Amount");
            table.addCell("₹ " + calculatedValues.get("total"));
            table.addCell("GST (@ " + request.getGstPercent() + "%)");
            table.addCell("₹ " + calculatedValues.get("gstAmount"));
            table.addCell("TDS (@ " + request.getTdsPercent() + "%)");
            table.addCell("₹ " + calculatedValues.get("tdsAmount"));
            table.addCell("Grand Total");
            table.addCell("₹ " + calculatedValues.get("grandTotal"));
            document.add(table);

            document.close();
            response.put("message", "Bill generated successfully");
            response.put("file", filename);
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }*/
}