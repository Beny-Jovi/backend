package com.marketplace.UserAccountManagement.domain;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    
    public void saveRole(Role role) {
        Objects.requireNonNull(role);
        roleRepository.save(role);
    }

    public Role getOrCreateRoleAccount(Role.RoleEnum roleEnum) {
        Role role = roleRepository.findAll()
            .stream()
            .filter(roleName -> roleName.getRoleName().equals(roleEnum))
            .findAny()
            .orElse(null);
        if (role == null) {
            Role newRole = new Role(roleEnum);
            saveRole(newRole);
            return newRole;
        }
        return role;
    }

}
