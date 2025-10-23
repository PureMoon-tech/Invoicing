package com.example.invc_proj.service;


import com.example.invc_proj.dto.ReceiptRequestDTO;
import com.example.invc_proj.dto.ReceiptResponseDTO;
import com.example.invc_proj.model.Enum.InvoiceStatus;
import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.model.Receipt;
import com.example.invc_proj.repository.InvoiceRepo;
import com.example.invc_proj.repository.ReceiptRepo;
import com.example.invc_proj.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepo receiptRepository;

    @Autowired
    private InvoiceRepo invoiceRepository;

    @Autowired
    private UserRepo userRepository;

    //  Used for scheduled or reconciliation-driven receipt generation
    public void generateAndSave(Invoice invoice, BigDecimal paymentAmount) {
        Receipt receipt = new Receipt();

        receipt.setInvoice(invoice);
        receipt.setAmount(paymentAmount);
        receipt.setPayment_date(LocalDateTime.now());
        receipt.setPayment_mode("BANK"); // default mode for auto-recon
        receipt.setReference("AUTO-" + UUID.randomUUID());
        receipt.setRemarks("Automated reconciliation payment");

        // If you want to tag the system user
        userRepository.findByUsername("system").ifPresent(receipt::setAcknowledgedBy);

        receiptRepository.save(receipt);
    }

    // Used for manual receipt creation
    public ReceiptResponseDTO addReceipt(ReceiptRequestDTO request) {
        Receipt receipt = new Receipt();

        LocalDateTime paymentDate = request.getPaymentDate() != null ? request.getPaymentDate() : LocalDateTime.now();
        receipt.setPayment_date(paymentDate);
        receipt.setPayment_mode(request.getPaymentMode());
        receipt.setReference(request.getReference());
        receipt.setRemarks(request.getRemarks());

        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        BigDecimal paidSoFar = invoice.getAmountPaid() != null ? invoice.getAmountPaid() : BigDecimal.ZERO;
        BigDecimal totalAmount = invoice.getTotal();
        BigDecimal thisAmount = BigDecimal.valueOf(request.getAmount());
        BigDecimal newPaid = paidSoFar.add(thisAmount);

        if (newPaid.compareTo(totalAmount) > 0) {
            throw new RuntimeException("Payment exceeds invoice amount "+paidSoFar+" "+thisAmount+" "+newPaid+" "+totalAmount);
        }

        // Update invoice state
        invoice.setAmountPaid(newPaid);
        if (newPaid.compareTo(totalAmount) == 0) {
            invoice.setStatus(InvoiceStatus.PAID);
            //invoice.setPaidDate(LocalDateTime.now());
        } else {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        }

        invoiceRepository.save(invoice);

        receipt.setInvoice(invoice);
        receipt.setAmount(thisAmount);
        receipt.setAcknowledgedBy(userRepository.findById(request.getAcknowledgedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found")));

        return mapToResponse(receiptRepository.save(receipt));
    }

    public List<ReceiptResponseDTO> getReceiptsByInvoiceId(int invoiceId) {
        return receiptRepository.findByInvoice_invoice_id(invoiceId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ReceiptResponseDTO getReceiptById(int receiptId) {
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Receipt not found"));
        return mapToResponse(receipt);
    }

    private ReceiptResponseDTO mapToResponse(Receipt receipt) {
        ReceiptResponseDTO dto = new ReceiptResponseDTO();
        dto.setReceiptId(receipt.getReceipt_id());
        dto.setInvoiceId(receipt.getInvoice().getInvoice_id());
        dto.setAmount(receipt.getAmount());
        dto.setPaymentDate(receipt.getPayment_date());
        dto.setPaymentMode(receipt.getPayment_mode());
        dto.setReference(receipt.getReference());
        dto.setAcknowledgedBy(
                receipt.getAcknowledgedBy() != null ? receipt.getAcknowledgedBy().getUsername() : "System"
        );
        dto.setRemarks(receipt.getRemarks());
        dto.setInsertedOn(receipt.getInserted_on());
        return dto;
    }
}
