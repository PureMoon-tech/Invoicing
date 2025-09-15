package com.example.invc_proj.repository;

import com.example.invc_proj.model.Enum.InvoiceStatus;
import com.example.invc_proj.model.Enum.QuoteStatus;
import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuoteRepo extends JpaRepository<Quote,Long> {

    @Query(value = "SELECT q FROM Quote q WHERE q.client.id = :clientId AND q.status IN :statuses ORDER BY q.quote_generated_date ASC",nativeQuery = true)
    Optional<Invoice> findTopByClient_idAndStatusInOrderByQuote_generated_dateAsc(int client_id, List<InvoiceStatus> statuses);

    @Query(value = "SELECT q FROM Quote q WHERE q.status = :p_quote_status ORDER BY q.quote_generated_date ASC")
    List<Quote> findAllByStatus( QuoteStatus p_quote_status);



}
