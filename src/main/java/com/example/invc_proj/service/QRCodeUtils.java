package com.example.invc_proj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;


public class QRCodeUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;

    /**
     * Generates QR code from any DTO/object as JSON.
     * @param dto The object to encode (any type).
     * @return BufferedImage of the QR code.
     * @throws JsonProcessingException If serialization fails.
     * @throws WriterException If QR encoding fails.
     */
    public static <T> BufferedImage generateQrCode(T dto) throws JsonProcessingException, WriterException {
        String qrData = objectMapper.writeValueAsString(dto);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * Generates QR code and saves to file.
     * @param dto The object to encode.
     * @param filePath Path to save PNG file.
     * @throws Exception If generation or file write fails.
     */
    public static <T> void generateQrCodeToFile(T dto, String filePath) throws Exception {
        BufferedImage qrImage = generateQrCode(dto);
        ImageIO.write(qrImage, "PNG", new File(filePath));
    }

    /**
     * Generates QR code as byte array (for embedding in PDF or API response).
     * @param dto The object to encode.
     * @return PNG bytes of the QR code.
     * @throws Exception If generation fails.
     */
    public static <T> byte[] generateQrCodeAsBytes(T dto) throws Exception {
        BufferedImage qrImage = generateQrCode(dto);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", baos);
        return baos.toByteArray();
    }
}