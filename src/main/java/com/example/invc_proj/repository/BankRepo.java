package com.example.invc_proj.repository;

import com.example.invc_proj.dto.BankDropdownDTO;
import com.example.invc_proj.dto.ClientDropdownDTO;
import com.example.invc_proj.model.BankDetails;
import com.example.invc_proj.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankRepo extends JpaRepository<BankDetails, Integer> {

   /* @Query("SELECT new com.example.invc_proj.dto.BankDropdownDTO" +
            "(b.bank_id,b.bank_name,b.account_no,b.account_holder_name) " + "FROM BankDetails b")
    List<BankDropdownDTO> findBanksForDropdown();*/

    @Query(value = "SELECT bank_id, bank_name, account_no, account_holder_name " +
            "FROM bank_details", nativeQuery = true)
    List<BankDropdownDTO> findBanksForDropdown();

    @Query(value = "SELECT bank_id, bank_name, account_no, account_holder_name " +
            "FROM bank_details WHERE bank_id = :Bank_id", nativeQuery = true)
    List<BankDropdownDTO> findBanksForDropdownById(@Param("Bank_id") int id);

}
