package com.marketplace.Account;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    // save role
    // get roll
    // add role
    // delete role
    // get and create 
    
    public void saveRole(Role role) {
        Objects.requireNonNull(role);
        roleRepository.save(role);
    }

    public Role getOrCreateRoleAccount(RoleEnum roleEnum) {
        Role role = roleRepository.findAll()
            .stream()
            .filter(roleName -> roleName.getRoleName().equals(roleEnum.name()))
            .findAny()
            .orElse(null);
        if (role == null) {
            Role newRole = new Role(roleEnum.name());
            saveRole(newRole);
            return newRole;
        }
        return role;
    }

}
