package com.example.invc_proj.service;

import com.example.invc_proj.dto.BankTransactionDTO;
import com.example.invc_proj.model.BankStatementUpload;
import com.example.invc_proj.model.Client;
import com.example.invc_proj.model.Enum.InvoiceStatus;
import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.model.ReconciliationLog;
import com.example.invc_proj.repository.BankStatementUploadRepository;
import com.example.invc_proj.repository.ClientRepo;
import com.example.invc_proj.repository.InvoiceRepo;
import com.example.invc_proj.repository.ReconciliationLogRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class BankReconciliationService {
/*
    private final BankStatementUploadRepository uploadRepo;
    private final InvoiceRepo invoiceRepo;
    private final ClientRepo clientRepo;
    private final ReceiptService receiptService;
    private final ReconciliationLogRepository logRepo;

    public BankReconciliationService(BankStatementUploadRepository uploadRepo,
                                     InvoiceRepo invoiceRepo,
                                     ClientRepo clientRepo,
                                     ReceiptService receiptService,
                                     ReconciliationLogRepository logRepo) {
        this.uploadRepo = uploadRepo;
        this.invoiceRepo = invoiceRepo;
        this.clientRepo = clientRepo;
        this.receiptService = receiptService;
        this.logRepo = logRepo;
    }

    public void saveUploadedFile(MultipartFile file) throws IOException {
        BankStatementUpload entity = new BankStatementUpload();
        entity.setFilename(file.getOriginalFilename());
        entity.setFileType(file.getOriginalFilename().endsWith(".pdf") ? "PDF" : "EXCEL");
        entity.setUploadedAt(LocalDateTime.now());
        entity.setProcessed(false);
        entity.setFileData(file.getBytes());
        uploadRepo.save(entity);
    }

    @Scheduled(cron = "0 0 0 * * *") // Run every midnight
    public void runReconciliationJob() {
        List<BankStatementUpload> pendingFiles = uploadRepo.findByProcessedFalse();
        for (BankStatementUpload file : pendingFiles) {
            try {
                List<BankTransactionDTO> transactions = parseTransactions(file);
                processTransactions(transactions, file);
                file.setProcessed(true);
            } catch (Exception e) {
                // log error
            }
        }
        uploadRepo.saveAll(pendingFiles);
    }

    private List<BankTransactionDTO> parseTransactions(BankStatementUpload file) throws IOException {
        if (file.getFileType().equals("EXCEL")) {
            return parseExcel(new ByteArrayInputStream(file.getFileData()));
        } else {
            return parsePdf(new ByteArrayInputStream(file.getFileData()));
        }
    }

    private void processTransactions(List<BankTransactionDTO> txns, BankStatementUpload file) {
        txns.parallelStream().forEach(tx -> {
            try {
                reconcileTransaction(tx, file);
            } catch (Exception e) {
                logReconciliationError(file, tx, e.getMessage());
            }
        });
    }

    private void reconcileTransaction(BankTransactionDTO tx, BankStatementUpload file) {
        Optional<Client> clientOpt = clientRepo.findByUpiIdOrAccountNumber(tx.getPayerId(),tx.getPayerId());
        if (clientOpt.isEmpty()) return;

        Client client = clientOpt.get();
        Optional<Invoice> invoiceOpt = invoiceRepo.findTopByClient_idAndStatusInOrderByInvoice_generated_dateAsc(
                client.getClient_id(), List.of(InvoiceStatus.UNPAID, InvoiceStatus.PARTIALLY_PAID));

        if (invoiceOpt.isEmpty()) return;

        Invoice invoice = invoiceOpt.get();
        BigDecimal alreadyPaid = Optional.ofNullable(invoice.getAmountPaid()).orElse(BigDecimal.ZERO);
        BigDecimal remaining = invoice.getTotal().subtract(alreadyPaid);

        if (tx.getAmount().compareTo(remaining) > 0) return; // Overpayment, skip or log

        BigDecimal newPaid = alreadyPaid.add(tx.getAmount());
        invoice.setAmountPaid(newPaid);

        if (newPaid.compareTo(invoice.getTotal()) >= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
            //invoice.setPaidDate(tx.getDate());
        } else {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        }

        invoiceRepo.save(invoice);
        receiptService.generateAndSave(invoice, tx.getAmount());

        ReconciliationLog log = new ReconciliationLog();
        log.setStatement(file);
        log.setTransactionRef(tx.getReference());
        log.setMatchedInvoiceId(invoice.getInvoice_id());
        log.setStatus(invoice.getStatus());
        logRepo.save(log);
    }

    private void logReconciliationError(BankStatementUpload file, BankTransactionDTO tx, String error) {
        ReconciliationLog log = new ReconciliationLog();
        log.setStatement(file);
        log.setTransactionRef(tx.getReference());
        log.setError(error);
        logRepo.save(log);
    }

    // EXCEL PARSER
    private List<BankTransactionDTO> parseExcel(InputStream is) throws IOException {
        List<BankTransactionDTO> transactions = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header
                LocalDate date = row.getCell(0).getLocalDateTimeCellValue().toLocalDate();
                BigDecimal amount = BigDecimal.valueOf(row.getCell(1).getNumericCellValue());
                String description = row.getCell(2).getStringCellValue();
                String reference = UUID.randomUUID().toString();
                String payerId = extractPayerId(description);
                transactions.add(new BankTransactionDTO(date, amount, reference, payerId, description));
            }
        }
        return transactions;
    }

    // PDF PARSER
    private List<BankTransactionDTO> parsePdf(InputStream is) throws IOException {
        List<BankTransactionDTO> transactions = new ArrayList<>();
        try (PDDocument doc = PDDocument.load(is)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);
            for (String line : text.split("\n")) {
                if (line.matches(".*\\d{4}-\\d{2}-\\d{2}.*")) {
                    // Simplified example; customize parsing logic to your format
                    String[] parts = line.split("\\s+");
                    LocalDate date = LocalDate.parse(parts[0]);
                    BigDecimal amount = new BigDecimal(parts[1]);
                    String description = String.join(" ", Arrays.copyOfRange(parts, 2, parts.length));
                    String reference = UUID.randomUUID().toString();
                    String payerId = extractPayerId(description);
                    transactions.add(new BankTransactionDTO(date, amount, reference, payerId, description));
                }
            }
        }
        return transactions;
    }

    private String extractPayerId(String description) {
        Matcher m = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-z]+|") // UPI format
                .matcher(description);
        return m.find() ? m.group() : null;
    }
*/

}
