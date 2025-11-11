package com.example.invc_proj.controller;


import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.service.QRCodeGeneratorService;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qr")
public class QRController {

    private final QRCodeGeneratorService qrCodeGeneratorService;

    @GetMapping("/generate")
    public byte[] InvoiceQR(@RequestBody Invoice invoice) throws IOException, WriterException {
        // Logic to generate QR code for an invoice

        byte[] qr = qrCodeGeneratorService.generateQrCodeAsBytes(invoice);
        return qr;
    }
}
