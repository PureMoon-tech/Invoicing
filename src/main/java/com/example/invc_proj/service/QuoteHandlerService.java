package com.example.invc_proj.service;

import com.example.invc_proj.model.*;
import com.example.invc_proj.repository.*;
import lombok.RequiredArgsConstructor;
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
public class QuoteHandlerService {

    private final QuoteRepo quoteRepo;
    private final ServicesQuotedRepo servicesQuotedRepo;
    private final ServicesRepo servicesRepository;
    private final QuotePdfService quotePdfService;


    private final ExecutorService pdfExecutor = Executors.newFixedThreadPool(10);

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<byte[]> getQuotePdfAsync(Quote quote) {
        return CompletableFuture.supplyAsync(() -> {
            List<ServicesQuoted> services = servicesQuotedRepo.findByQuoteId(quote.getQuote_id());


            List<Integer> serviceIds = services.stream()
                    .map(ServicesQuoted::getService_id)
                    .collect(Collectors.toList());

            Map<Integer, Services> serviceMap = servicesRepository.findAllById(serviceIds)
                    .stream()
                    .collect(Collectors.toMap(Services::getService_id, s -> s));

            return quotePdfService.generateQuotePdf(quote, services,serviceMap);
        }, pdfExecutor);
    }

}
