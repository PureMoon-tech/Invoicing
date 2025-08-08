package com.example.invc_proj.repository;

import com.example.invc_proj.model.Client;
import com.example.invc_proj.model.Enum.InvoiceStatus;
import com.example.invc_proj.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, Long> {

    // Fetch the earliest unpaid or partially paid invoice for a client
    @Query(value = "SELECT i FROM Invoice i WHERE i.client.id = :clientId AND i.status IN :statuses ORDER BY i.Invoice_generated_date ASC",nativeQuery = true)
    Optional<Invoice> findTopByClient_idAndStatusInOrderByInvoice_generated_dateAsc(int client_id, List<InvoiceStatus> statuses);

//    Optional<Invoice> findTopByClientAndStatusInOrderByIssueDateAsc(@Param("clientId") int clientId, @Param("statuses") List<InvoiceStatus> statuses);

    //Optional<Invoice> findTopByClient_idAndStatusInOrderByInvoice_generated_dateAsc(int client_id, List<InvoiceStatus> statuses);

}
