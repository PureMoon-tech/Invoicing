package com.example.invc_proj.dto;

import lombok.Data;

@Data
public class BankDropdownDTO {

    private int bank_id;
    private String bank_name;
    private String account_no;
    private String account_holder_name;

    public BankDropdownDTO(int bank_id, String bank_name, String account_no, String account_holder_name)
    {
        this.bank_id = bank_id;
        this.bank_name = bank_name;
        this.account_no = account_no;
        this.account_holder_name = account_holder_name;
    }

}
