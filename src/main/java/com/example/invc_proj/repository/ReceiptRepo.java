package com.example.invc_proj.repository;

import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReceiptRepo extends JpaRepository<Receipt, Integer> {

    //List<Receipt> findByInvoice_invoice_id(int invoiceId);

    @Query("SELECT r FROM Receipt r WHERE r.invoice.invoice_id = :invoiceId")
    List<Receipt> findByInvoice_invoice_id(@Param("invoiceId") int invoiceId);

}
