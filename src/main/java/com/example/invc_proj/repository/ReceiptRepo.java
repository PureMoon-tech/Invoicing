package com.example.invc_proj.repository;

import com.example.invc_proj.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReceiptRepo extends JpaRepository<Receipt, Integer> {

    List<Receipt> findByInvoiceId(int invoiceId);
}
