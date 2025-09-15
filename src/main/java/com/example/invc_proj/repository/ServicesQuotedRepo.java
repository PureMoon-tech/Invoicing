package com.example.invc_proj.repository;

import com.example.invc_proj.model.ServicesQuoted;
import com.example.invc_proj.model.ServicesRequested;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServicesQuotedRepo extends JpaRepository<ServicesQuoted,Integer> {
    @Query("SELECT s FROM ServicesQuoted s WHERE s.quote_id.quote_id = :quoteId")
    List<ServicesQuoted> findByQuoteId(@Param("quoteId") Long quoteId);

}
