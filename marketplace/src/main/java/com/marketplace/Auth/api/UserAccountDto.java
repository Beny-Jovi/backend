package com.marketplace.Auth.api;

import com.marketplace.Auth.domain.Role;

import java.util.Set;

public record UserAccountDto(String email
        , Set<Role.RoleEnum> roles
) {
}
