package com.marketplace.Auth;

import com.marketplace.Auth.domain.Role;
import com.marketplace.Auth.domain.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RoleServiceUnitTest {

    @InjectMocks
    private RoleService roleService;
    private Role role;

    @BeforeEach
    protected void setup() {

        Set<Role> roles = new HashSet<>();
        role = new Role(Role.RoleEnum.BUYER);
        roles.add(role);

    }

    @RepeatedTest(3)
    public void getOrSaveRoleTest_andReturnRole() {
        Role role1 = roleService.getOrCreateRoleAccount(Role.RoleEnum.DEVELOPER);
        assertThat(role1.getRoleName()).isEqualTo(Role.RoleEnum.DEVELOPER);
    }

    @RepeatedTest(3)
    public void getRoleFromGetOrSaveRoleTestMethod_andReturnRole() {
        Role savedRole = roleService.saveRoleTest(Role.RoleEnum.DEVELOPER);
        assertThat(savedRole.getRoleName()).isEqualTo(Role.RoleEnum.DEVELOPER);
        Role role1 = roleService.getOrCreateRoleAccount(Role.RoleEnum.DEVELOPER);
        assertThat(role1.getRoleName()).isEqualTo(Role.RoleEnum.DEVELOPER);
    }
}
