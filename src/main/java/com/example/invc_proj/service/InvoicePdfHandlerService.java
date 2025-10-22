package com.example.invc_proj.service;

import com.example.invc_proj.model.*;
import com.example.invc_proj.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoicePdfHandlerService {

    private final InvoiceRepo invoiceRepo;

    private final ServicesRequestedRepo servicesRequestedRepo;

    private final InvoicePdfService invoicePdfService;

    private final ClientRepo clientRepository;

    private final BankRepo bankRepository;

    private final ServicesRepo servicesRespository;

    public byte[] getInvoicePdf(Invoice invoice) {
        List<ServicesRequested> services = servicesRequestedRepo.findByInvoiceId(invoice.getInvoice_id());
        return invoicePdfService.generateInvoicePdf(invoice, services);
    }

    private final ExecutorService pdfExecutor = Executors.newFixedThreadPool(10);

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<byte[]> getInvoicePdfAsync(Invoice invoice) {
        return CompletableFuture.supplyAsync(() -> {
            List<ServicesRequested> services = servicesRequestedRepo.findByInvoiceId(invoice.getInvoice_id());

            Client client = clientRepository.findById(invoice.getClient_id()).orElseThrow(()-> new RuntimeException("Client Details Not Fuund"));
            BankDetails bankDetails = bankRepository.findById(invoice.getBank_id()).orElseThrow(()-> new RuntimeException("Bank Details Not Fuund"));


            List<Integer> serviceIds = services.stream()
                    .map(ServicesRequested::getService_id)
                    .collect(Collectors.toList());

            Map<Integer, Services> serviceMap = servicesRespository.findAllById(serviceIds)
                    .stream()
                    .collect(Collectors.toMap(Services::getService_id, s -> s));

            return invoicePdfService.generateInvoicePdf(invoice, services,client,bankDetails,serviceMap);
        }, pdfExecutor);
    }
}
