package com.example.invc_proj.service;


import com.example.invc_proj.model.Enum.QuoteType;
import com.example.invc_proj.model.QuoteSequence;
import com.example.invc_proj.repository.QuoteSequenceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Year;

@Service
public class QuoteNumGeneratorService {

    @Autowired
    private QuoteSequenceRepo sequenceRepository;

    @Transactional
    public String generateQuoteNumber(QuoteType type) {
        String prefix = switch (type) {
            case SERVICE -> "QTE-SRV";
            case GOODS -> "QTE-GDS";
            case RECURRING -> "QTE-RCG";
            default -> "QTE";
        };

        String financialYear = String.valueOf(Year.now().getValue());

        QuoteSequence sequence = sequenceRepository
                .lockByQuoteTypeAndYear(type.name(), financialYear)
                .orElseGet(() -> {
                    QuoteSequence newSeq = new QuoteSequence();
                    newSeq.setQuoteType(type.name());
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
