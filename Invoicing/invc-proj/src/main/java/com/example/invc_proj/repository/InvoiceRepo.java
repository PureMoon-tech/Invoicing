package com.example.invc_proj.repository;

import com.example.invc_proj.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, Integer> {
}
