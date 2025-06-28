package com.example.invc_proj.repository;

import com.example.invc_proj.model.InvoiceSequence;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceSequenceRepo extends JpaRepository<InvoiceSequence, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM InvoiceSequence s WHERE s.invoiceType = :invoiceType AND s.financialYear = :financialYear")
    Optional<InvoiceSequence> lockByInvoiceTypeAndYear(
            @Param("invoiceType") String invoiceType,
            @Param("financialYear") String financialYear
    );
}

