package com.example.invc_proj.repository;


import com.example.invc_proj.dto.InvoiceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface InvoiceViewRepo extends JpaRepository<InvoiceView,Long> {

    List<InvoiceView> findByClientNameContainingIgnoreCase(String name);
    List<InvoiceView> findByInvoiceGeneratedDateBetween(Date from, Date to);
    List<InvoiceView> findByTotalGreaterThanEqual(BigDecimal min);

    // Prevent accidental mutations (optional but safe)
    @Override
    default <S extends InvoiceView> S save(S entity) {
        throw new UnsupportedOperationException("InvoiceView is read-only");
    }

    @Override
    default void delete(InvoiceView entity) {
        throw new UnsupportedOperationException("InvoiceView is read-only");
    }
}
