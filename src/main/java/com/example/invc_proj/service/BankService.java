package com.example.invc_proj.service;

import com.example.invc_proj.dto.BankDetailsDTO;
import com.example.invc_proj.dto.BankDropdownDTO;
import com.example.invc_proj.exceptions.NotFoundException;
import com.example.invc_proj.mapper.BankDetailsDTOMapper;
import com.example.invc_proj.model.BankDetails;
import com.example.invc_proj.repository.BankRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BankService {

    private final BankRepo repository_bnk;

    @Autowired
    public BankService(BankRepo repository_bnk)
    {
        this.repository_bnk = repository_bnk;
    }

    public List<BankDetailsDTO> getAllBankDetails()
    {
        List<BankDetailsDTO> bankDTOList = repository_bnk.findAll()
                .stream()
                .map(BankDetailsDTOMapper::toDTO)
                .collect(Collectors.toList());
         return  bankDTOList;
    }

    public BankDetails getBankDetailsById(int id)
    {
        //System.out.println("get by id"+id);
        try{
            return repository_bnk.findById(id).orElseThrow(() -> new NotFoundException("Bank with ID " + id + " not found."));
        }
        catch (Exception e)
        {
            throw new NotFoundException("Bank with ID " + id + " not found.");
        }

    }

    public List<BankDropdownDTO> getBankDropdown()
    {

        return repository_bnk.findBanksForDropdown();
    }

    public List<BankDropdownDTO> getBankDropdownById(int id)
    {
        try
        {
            return repository_bnk.findBanksForDropdownById(id);
        }
        catch (Exception e)
        {
            throw new NotFoundException("Bank with ID " + id + " not found.");
        }

    }


    public void addBankDetails(BankDetailsDTO bnk)
    {
        BankDetails Bank = BankDetailsDTOMapper.toEntity(bnk);
        try {
            repository_bnk.save(Bank);
        } catch (Exception e) {
            throw new RuntimeException("Error saving bank details: " /*+ e.getMessage()*/, e);
        }
    }

    public void deleteBankDetails(int pBankId)
    {
      BankDetails bankToDelete = repository_bnk.findById(pBankId)
              .orElseThrow(() -> new NotFoundException("Bank with ID " + pBankId + " not found."));
      repository_bnk.deleteById(pBankId);
    }
}
