package com.example.invc_proj.repository;

import com.example.invc_proj.model.BankDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepo extends JpaRepository<BankDetails, Integer> {

}
