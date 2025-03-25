package com.example.invc_proj.controller;

import com.example.invc_proj.dto.BankDropdownDTO;
import com.example.invc_proj.model.BankDetails;
import com.example.invc_proj.model.BankDetailsDropDown;
import com.example.invc_proj.service.BankService;
import com.example.invc_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bank-config")
public class BankController {

    @Autowired
    private BankService service;

    @GetMapping("/bankdetails")
    public List<BankDetails> getAllBankDetails()
    {
        return service.getAllBankDetails();
    }


    @GetMapping("/bankdetailsdropdown")
    public ResponseEntity<BankDropdownDTO> getAllBankDetailsDD()
    {

        return service.getBankDropdown();

       /* List<BankDetails> bankDetailsList= service.getAllBankDetails();; // Retrieve data from your database
        List<BankDropdownDTO> BankDetailsDD = new ArrayList<>();

        for (BankDetails bankDetails : bankDetailsList) {
            BankDropdownDTO simplified = new BankDropdownDTO();
            simplified.setBd_id(bankDetails.getBd_id());
            simplified.setBank_name(bankDetails.getBank_name());
            simplified.setAccount_no(bankDetails.getAccount_no());
            simplified.setAccount_holder_name(bankDetails.getAccount_holder_name());
            BankDetailsDD.add(simplified);
        }

        return BankDetailsDD;
       */

    }


    @GetMapping("/bankdetails/{id}")
    public Optional<BankDetails> getBankDetailsById(@PathVariable int id)
    {
        System.out.println("calling getBankDetailsById");
        return service.getBankDetailsById(id);
    }

    @PostMapping("/bankdetails")
    public void addClients(@RequestBody BankDetails bnk)
    {
        System.out.println("calling post");
        System.out.println(bnk);
        service.addBankDetails(bnk);
    }
}
