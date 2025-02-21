package com.marketplace.Account;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class Mapper {
    
    public AccountDTO toAccountDto(Seller seller) {
        String name = seller.getName();
        String email = seller.getEmail();

        Set<String> roles = seller
            .getAccountRoles()
            .stream()
            .map(Role::getRoleName)
            .collect(Collectors.toSet());
        return new AccountDTO(name, email, roles);
    }

    public Seller toSellerAccount(AccountCreationDTO accountDTO) {
        return new Seller(accountDTO.getSellerEmail(), accountDTO.getSellerName(), accountDTO.getSellerPassword(), new HashSet<Role>());
    }

}
