package com.example.invc_proj.repository;

import com.example.invc_proj.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceNextValRepo extends JpaRepository<Invoice, Long>
{
    @Query(value = "SELECT public.get_next_sequence_value(:seqName)", nativeQuery = true)
    int getNextSequenceValue(@Param("seqName") String seqName);
}

