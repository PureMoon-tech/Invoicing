package com.example.invc_proj.repository;

import com.example.invc_proj.model.InvoiceSequence;
import com.example.invc_proj.model.QuoteSequence;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuoteSequenceRepo extends JpaRepository<QuoteSequence,Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM QuoteSequence s WHERE s.quoteType = :quoteType AND s.financialYear = :financialYear")
    Optional<QuoteSequence> lockByQuoteTypeAndYear(
            @Param("quoteType") String quoteType,
            @Param("financialYear") String financialYear
    );
}
