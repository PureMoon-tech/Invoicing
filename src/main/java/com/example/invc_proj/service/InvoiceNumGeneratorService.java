package com.example.invc_proj.service;


import com.example.invc_proj.model.InvoiceSequence;
import com.example.invc_proj.model.Enum.InvoiceType;
import com.example.invc_proj.repository.InvoiceSequenceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;

@Service
public class InvoiceNumGeneratorService {

    @Autowired
    private InvoiceSequenceRepo sequenceRepository;

    @Transactional
    public String generateInvoiceNumber(InvoiceType type) {
        String prefix = switch (type) {
            case SERVICE -> "SRV";
            case GOODS -> "GDS";
            case RECURRING -> "RCG";
            default -> "INV";
        };

        String financialYear = String.valueOf(Year.now().getValue());

        InvoiceSequence sequence = sequenceRepository
                .lockByInvoiceTypeAndYear(type.name(), financialYear)
                .orElseGet(() -> {
                    InvoiceSequence newSeq = new InvoiceSequence();
                    newSeq.setInvoiceType(type.name());
                    newSeq.setFinancialYear(financialYear);
                    newSeq.setCurrentSequence(0);
                    return newSeq;
                });

        int next = sequence.getCurrentSequence() + 1;
        sequence.setCurrentSequence(next);

        sequenceRepository.save(sequence);

        return String.format("%s-%s-%04d", prefix, financialYear, next);
    }
}
