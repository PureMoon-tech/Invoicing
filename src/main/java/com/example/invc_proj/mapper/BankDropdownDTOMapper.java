package com.example.invc_proj.mapper;

import com.example.invc_proj.dto.BankDropdownDTO;
import com.example.invc_proj.model.BankDetails;

public class BankDropdownDTOMapper {

    public static BankDropdownDTO toBankDropdownDTO(BankDetails bankDetails)
    {
          if (bankDetails == null)
          {
              return null;
          }

          return new BankDropdownDTO( bankDetails.getBank_id(),
                                      bankDetails.getBank_name(),
                                      bankDetails.getAccount_no(),
                                      bankDetails.getAccount_holder_name());
    }

    public static BankDetails tobankdetails(BankDropdownDTO bankDropdownDTO)
    {
        if (bankDropdownDTO == null)
        {
            return null;
        }
        BankDetails bankDetails = new BankDetails();

         bankDetails.setBank_id(bankDropdownDTO.getBank_id());
         bankDetails.setBank_name(bankDropdownDTO.getBank_name());
         bankDetails.setAccount_no(bankDropdownDTO.getAccount_no());
         bankDetails.setAccount_holder_name(bankDropdownDTO.getAccount_holder_name());
        return bankDetails;

    }


}
