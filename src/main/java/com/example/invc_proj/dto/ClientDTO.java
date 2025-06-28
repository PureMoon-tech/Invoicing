package com.example.invc_proj.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ClientDTO {

    private String client_name;
    private String client_type;
    private String primary_mobile_number;
    private String primary_email_id;
    private String secondary_mobile_number;
    private String secondary_email_id;
    private String occupation;
    private String pan_no;
    private String GSTN;
    private String aadhaar_no;
    private String address;
    private String pincode;


    public ClientDTO(String client_name, String client_type, String primary_mobile_number,String primary_email_id,
                     String secondary_mobile_number,String secondary_email_id, String occupation, String pan_no,
                     String GSTN, String aadhaar_no, String address, String pincode)
    {
        this.client_name = client_name;
        this.client_type = client_type;
        this.primary_mobile_number = primary_mobile_number;
        this.primary_email_id = primary_email_id;
        this.secondary_mobile_number = secondary_mobile_number;
        this.secondary_email_id = secondary_email_id;
        this.occupation = occupation;
        this.pan_no = pan_no;
        this.GSTN = GSTN;
        this.aadhaar_no = aadhaar_no;
        this.address = address;
        this.pincode = pincode;
    }


}
