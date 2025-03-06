package com.marketplace.Account;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor 
public class AccountDTO {

    private String sellerEmail;
    private String sellerName;
    private Set<String> roles;

    public AccountDTO(String sellerEmail, String sellerName, Set<String> roles) {
        this.setSellerEmail(sellerEmail);
        this.setSellerName(sellerName);
        this.setRoles(roles);
    }

}
