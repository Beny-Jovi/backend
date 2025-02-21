package com.marketplace.Account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor 
public class AccountCreationDTO {
    
    private String sellerEmail;
    private String sellerName;
    private String sellerPassword;

    public AccountCreationDTO(String sellerEmail, String sellerName, String sellerPassword) {
        this.setSellerEmail(sellerEmail);
        this.setSellerName(sellerName);
        this.setSellerPassword(sellerPassword);
    }

}
