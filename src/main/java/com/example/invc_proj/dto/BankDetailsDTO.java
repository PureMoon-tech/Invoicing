package com.example.invc_proj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankDetailsDTO
            {

                private String account_holder_name;
                private String bank_name;
                private String branch;
                private String account_no;
                private String ifsc_code;
                private String micr_code;
                private String upi_number;
            }
