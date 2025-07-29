package com.example.invc_proj.mapper;
import com.example.invc_proj.dto.BankDetailsDTO;
import com.example.invc_proj.model.BankDetails;

public class BankDetailsDTOMapper {

    // Convert Entity to DTO
    public static BankDetailsDTO toDTO(BankDetails entity) {
        if (entity == null) return null;

        return new BankDetailsDTO(
                entity.getAccount_holder_name(),
                entity.getBank_name(),
                entity.getBranch(),
                entity.getAccount_no(),
                entity.getIfsc_code(),
                entity.getMicr_code(),
                entity.getUpi_number()
        );
    }

    // Convert DTO to Entity (without audit or timestamps)
    public static BankDetails toEntity(BankDetailsDTO dto) {
        if (dto == null) return null;

        BankDetails entity = new BankDetails();
        entity.setAccount_holder_name(dto.getAccount_holder_name());
        entity.setBank_name(dto.getBank_name());
        entity.setBranch(dto.getBranch());
        entity.setAccount_no(dto.getAccount_no());
        entity.setIfsc_code(dto.getIfsc_code());
        entity.setMicr_code(dto.getMicr_code());
        entity.setUpi_number(dto.getUpi_number());

        return entity;
    }
}

