package com.marketplace.StoreManagement.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("role_service_inStore_management")
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
