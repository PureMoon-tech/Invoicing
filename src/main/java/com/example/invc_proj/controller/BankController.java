package com.example.invc_proj.controller;

import com.example.invc_proj.dto.BankDetailsDTO;
import com.example.invc_proj.dto.BankDropdownDTO;
import com.example.invc_proj.exceptions.ApiResponse;
import com.example.invc_proj.exceptions.ApiResponses;
import com.example.invc_proj.model.BankDetails;
import com.example.invc_proj.model.BankDetailsDropDown;
import com.example.invc_proj.service.BankService;
import com.example.invc_proj.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bank-config")
@PreAuthorize("isAuthenticated()")
public class BankController
{

    @Autowired
    private BankService service;

    @GetMapping("/bankdetails")
    public ResponseEntity<ApiResponse<List<BankDetailsDTO>>> getAllBankDetails()
    {
        List<BankDetailsDTO> bankDetails = service.getAllBankDetails();
        return ApiResponses.ok(bankDetails);
    }


    @GetMapping("/bankdetailsdropdown")
    public ResponseEntity<ApiResponse<List<BankDropdownDTO>>> getAllBankDetailsDD()
    {
        List<BankDropdownDTO> dbdto = service.getBankDropdown();
        return ApiResponses.ok(dbdto);
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
    public ResponseEntity<ApiResponse<BankDetails>> getBankDetailsById(@PathVariable int id)
    {
        //System.out.println("calling getBankDetailsById");
        BankDetails bankDetails = service.getBankDetailsById(id);
        return ApiResponses.ok(bankDetails);
    }


    //private static final Logger logger = LoggerFactory.getLogger(BankDetailsController.class);  // Logger initialization

    @PostMapping("/bankdetails")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> addBankDetails(@RequestBody BankDetailsDTO bnk)
    {
            //logger.info("Received request to add bank details: {}", bnk);
            service.addBankDetails(bnk);  // Call the service to add bank details
            //return ResponseEntity.status(HttpStatus.CREATED).body("Bank details added successfully.");
              return ApiResponses.created("Bank details added successfully.");
    }

    @DeleteMapping("/bankdetails/{p_bank_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteBankDetails(@PathVariable int p_bank_id)
    {

            service.deleteBankDetails(p_bank_id);
            //return ResponseEntity.status(HttpStatus.OK).body("Bank Details Removed");
              return ApiResponses.ok("Bank Details Removed");

    }
}

