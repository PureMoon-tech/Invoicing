package com.example.invc_proj.repository;


import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.model.ServicesRequested;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ServicesRequestedRepo extends JpaRepository<ServicesRequested, Integer> {
    //List<ServicesRequested> findByInvoice(Invoice invoice);

    @Query("SELECT s FROM ServicesRequested s WHERE s.invoice_id.invoice_id = :invoiceId")
    List<ServicesRequested> findByInvoiceId(@Param("invoiceId") int invoiceId);

    List<ServicesRequested> findById(int invoice_id);


}
