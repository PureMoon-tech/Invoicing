package com.example.invc_proj.model;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class BankDetailsDropDown {

        private int bn_id;
        private String bank_name;
        private String account_no;
        private String account_holder_name;

        public int getBn_id() {
                return bn_id;
        }

        public void setBn_id(int bn_id) {
                this.bn_id = bn_id;
        }

        public void setAccount_no(String account_no) {
                this.account_no = account_no;
        }

        public void setBank_name(String bank_name) {
                this.bank_name = bank_name;
        }
}
