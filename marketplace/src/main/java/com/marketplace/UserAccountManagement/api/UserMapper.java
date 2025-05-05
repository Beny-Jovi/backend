package com.marketplace.UserAccountManagement.api;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.marketplace.UserAccountManagement.domain.Role;
import com.marketplace.UserAccountManagement.domain.User;

@Component
public class UserMapper {
    
    public UserAccountDTO toAccountDto(User seller) {
        String name = seller.getName();
        String email = seller.getEmail();

        Set<Role.RoleEnum> roles = seller
            .getAccountRoles()
            .stream()
            .map(Role::getRoleName)
            .collect(Collectors.toSet());
        return new UserAccountDTO(email, name, roles);
    }

    public User toUserAccount(UserAccountCreationDTO accountDTO) {
        return new User(accountDTO.email(), accountDTO.name(), accountDTO.password(), new HashSet<Role>());
    }

}
