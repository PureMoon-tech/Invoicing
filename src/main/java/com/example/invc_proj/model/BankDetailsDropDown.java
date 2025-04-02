package com.example.invc_proj.model;
import jakarta.persistence.Entity;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class BankDetailsDropDown {

        private int bank_id;
        private String bank_name;
        private String account_no;
        private String account_holder_name;

        public int getBn_id() {
                return bank_id;
        }

        public void setBn_id(int bn_id) {
                this.bank_id = bank_id;
        }

        public void setAccount_no(String account_no) {
                this.account_no = account_no;
        }

        public void setBank_name(String bank_name) {
                this.bank_name = bank_name;
        }

        public void setAccount_holder_name(String account_holder_name)
        {this.account_holder_name = account_holder_name;}
}
