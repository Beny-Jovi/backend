package com.marketplace.Account;

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

    public Role getOrCreateRoleAccount(RoleEnum roleEnum) {
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

    public void DeleteRole(String roleId) {
        Role foundRole = roleRepository.findAll()
        .stream()
        .filter(role -> role.getId().equals(roleId))
        .findAny()
        .orElse(null);
        if (foundRole != null) {
            roleRepository.delete(foundRole);
        }
    }

}
