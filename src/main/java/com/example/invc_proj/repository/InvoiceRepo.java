package com.example.invc_proj.repository;

import com.example.invc_proj.model.Client;
import com.example.invc_proj.model.Enum.InvoiceStatus;
import com.example.invc_proj.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, Long> {

    // Fetch the earliest unpaid or partially paid invoice for a client
    @Query(value = "SELECT i FROM Invoice i " +
            "WHERE i.client_id = :clientId " +
            "AND i.status IN :statuses "
            //"ORDER BY i.Invoice_generated_date ASC"
    )
    Optional<Invoice> findTopByClient_idAndStatusInOrderByInvoice_generated_dateAsc
    (int client_id, List<InvoiceStatus> statuses);

//    Optional<Invoice> findTopByClientAndStatusInOrderByIssueDateAsc(@Param("clientId") int clientId, @Param("statuses") List<InvoiceStatus> statuses);

    //Optional<Invoice> findTopByClient_idAndStatusInOrderByInvoice_generated_dateAsc(int client_id, List<InvoiceStatus> statuses);

    //Page<Invoice> findAllByInvoice_idContaining(Long invoice_id, Pageable pageable);

    @Query(value = "SELECT i FROM Invoice i " +
            "WHERE i.client_id = :clientId " +
            "AND i.status IN :statuses " +
            "AND (:invoiceId IS NULL OR i.id = :invoiceId) "
           // "ORDER BY i.invoice_generated_date ASC"
    )
    Page<Invoice> findByClientIdAndStatusesAndOptionalInvoiceId(
            @Param("clientId") int clientId,
            @Param("statuses") List<InvoiceStatus> statuses,
            @Param("invoiceId") Long invoiceId,
            Pageable pageable);


}
