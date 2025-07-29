package com.example.invc_proj.controller;

import com.example.invc_proj.dto.BankDetailsDTO;
import com.example.invc_proj.dto.BankDropdownDTO;
import com.example.invc_proj.model.BankDetails;
import com.example.invc_proj.model.BankDetailsDropDown;
import com.example.invc_proj.service.BankService;
import com.example.invc_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bank-config")
@PreAuthorize("isAuthenticated()")
public class BankController
{

    @Autowired
    private BankService service;

    @GetMapping("/bankdetails")
    public ResponseEntity<List<BankDetailsDTO>> getAllBankDetails()
    {
        List<BankDetailsDTO> bankDetails = service.getAllBankDetails();
        return ResponseEntity.ok(bankDetails);
    }


    @GetMapping("/bankdetailsdropdown")
    public ResponseEntity<List<BankDropdownDTO>> getAllBankDetailsDD()
    {
        List<BankDropdownDTO> dbdto = service.getBankDropdown();
        return ResponseEntity.ok(dbdto);
    }
       /* List<BankDetails> bankDetailsList= service.getAllBankDetails();; // Retrieve data from your database
        List<BankDropdownDTO> BankDetailsDD = new ArrayList<>();

        for (BankDetails bankDetails : bankDetailsList)
        {
            BankDropdownDTO simplified = new BankDropdownDTO();
            simplified.setBd_id(bankDetails.getBd_id());
            simplified.setBank_name(bankDetails.getBank_name());
            simplified.setAccount_no(bankDetails.getAccount_no());
            simplified.setAccount_holder_name(bankDetails.getAccount_holder_name());
            BankDetailsDD.add(simplified);
        }

        return BankDetailsDD;
       */




    @GetMapping("/bankdetails/{id}")
    public ResponseEntity<Optional<BankDetails>> getBankDetailsById(@PathVariable int id)
    {
        System.out.println("calling getBankDetailsById");

        Optional<BankDetails> bankDetails = service.getBankDetailsById(id);
        return ResponseEntity.ok(bankDetails);
    }


    //private static final Logger logger = LoggerFactory.getLogger(BankDetailsController.class);  // Logger initialization

    @PostMapping("/bankdetails")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addBankDetails(@RequestBody BankDetailsDTO bnk)
    {
        try {
            //logger.info("Received request to add bank details: {}", bnk);
            service.addBankDetails(bnk);  // Call the service to add bank details
            return ResponseEntity.status(HttpStatus.CREATED).body("Bank details added successfully.");
        } catch (Exception e) {
            //logger.error("Error while adding bank details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add bank details.");
        }
    }

    @DeleteMapping("/bankdetails/{p_bank_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteBankDetails(@PathVariable int p_bank_id)
    {
        try {
            service.deleteBankDetails(p_bank_id);
            return ResponseEntity.status(HttpStatus.OK).body("Bank Details Removed");
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete bank details");
        }
    }
}

