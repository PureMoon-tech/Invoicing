package com.example.invc_proj.service;

import com.example.invc_proj.dto.BankDropdownDTO;
import com.example.invc_proj.model.BankDetails;
import com.example.invc_proj.repository.BankRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankService {

    private final BankRepo repository_bnk;

    @Autowired
    public BankService(BankRepo repository_bnk)
    {
        this.repository_bnk = repository_bnk;
    }

    public List<BankDetails> getAllBankDetails()
    {
        return repository_bnk.findAll();
    }

    public Optional<BankDetails> getBankDetailsById(int id)
    {
        System.out.println("get by id"+id);
        return repository_bnk.findById(id);
    }

    public List<BankDropdownDTO> getBankDropdown()
    {
        return repository_bnk.findBanksForDropdown();
    }

    public void addBankDetails(BankDetails bnk)
    {
        repository_bnk.save(bnk);
    }

    public void deleteBankDetails(int pBankId)
    {
      repository_bnk.deleteById(pBankId);
    }
}
