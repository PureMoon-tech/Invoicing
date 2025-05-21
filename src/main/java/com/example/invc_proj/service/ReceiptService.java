package com.example.invc_proj.service;

import com.example.invc_proj.dto.ReceiptRequestDTO;
import com.example.invc_proj.dto.ReceiptResponseDTO;
import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.model.Receipt;
import com.example.invc_proj.repository.InvoiceRepo;
import com.example.invc_proj.repository.ReceiptRepo;
import com.example.invc_proj.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceiptService {

        @Autowired
        private ReceiptRepo receiptRepository;

        @Autowired
        private InvoiceRepo invoiceRepository;

        @Autowired
        private UserRepo userRepository;

        public ReceiptResponseDTO addReceipt(ReceiptRequestDTO request) {
            Receipt receipt = new Receipt();
            LocalDateTime PaymentDate = request.getPaymentDate();
            if(PaymentDate == null)
            {
                PaymentDate = LocalDateTime.now();
            }

            receipt.setPayment_date(PaymentDate);

            receipt.setPayment_mode(request.getPaymentMode());
            receipt.setReference(request.getReference());
            receipt.setRemarks(request.getRemarks());


            Invoice invc = invoiceRepository.findById(request.getInvoiceId()).orElseThrow(() -> new RuntimeException("Invoice not found"));
            receipt.setInvoice(invc);

            int Amount = invc.getTotal();
            String Status = invc.getStatus();

            if(Status.equalsIgnoreCase("UNPAID")) {
                if (Amount >= request.getAmount()) {
                    receipt.setAmount(request.getAmount());
                } else {
                    throw new RuntimeException("Enter Valid Invoice Amount");
                }
            }
            else if(Status.equalsIgnoreCase("PAID"))
              {
                  if (Amount == request.getAmount()) {
                      receipt.setAmount(request.getAmount());
                  } else {
                      throw new RuntimeException("Enter Valid Invoice Amount");
                  }
              }

            receipt.setAcknowledgedBy(userRepository.findById(request.getAcknowledgedByUserId())
                    .orElseThrow(() -> new RuntimeException("User not found")));

            receipt = receiptRepository.save(receipt);

            return mapToResponse(receipt);
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
            dto.setAcknowledgedBy(receipt.getAcknowledgedBy().getUsername()); // adjust based on your user fields
            dto.setRemarks(receipt.getRemarks());
            dto.setInsertedOn(receipt.getInserted_on());
            return dto;
        }
    }

