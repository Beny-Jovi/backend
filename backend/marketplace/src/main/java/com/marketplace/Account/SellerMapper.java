package com.marketplace.Account;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class SellerMapper {
    
    public SellerAccountDTO toAccountDto(Seller seller) {
        String name = seller.getName();
        String email = seller.getEmail();

        Set<RoleEnum> roles = seller
            .getAccountRoles()
            .stream()
            .map(Role::getRoleName)
            .collect(Collectors.toSet());
        return new SellerAccountDTO(email, name, roles);
    }

    public Seller toSellerAccount(SellerAccountCreationDTO accountDTO) {
        return new Seller(accountDTO.email(), accountDTO.name(), accountDTO.password(), new HashSet<Role>());
    }

}
