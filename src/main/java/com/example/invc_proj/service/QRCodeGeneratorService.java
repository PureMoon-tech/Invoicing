package com.example.invc_proj.service;

import com.example.invc_proj.model.Invoice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class QRCodeGeneratorService {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;

    public BufferedImage generateQrCode(Invoice invoice) throws IOException, WriterException {
        String qrData = objectMapper.writeValueAsString(invoice);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public void generateQrCodeToFile(Invoice invoice, String filePath) throws IOException, WriterException {
        BufferedImage qrImage = generateQrCode(invoice);
        ImageIO.write(qrImage, "PNG", new File(filePath));
    }

    public byte[] generateQrCodeAsBytes(Invoice invoice) throws IOException, WriterException {
        BufferedImage qrImage = generateQrCode(invoice);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", baos);
        return baos.toByteArray();
    }
}