package com.marketplace;

import com.marketplace.Auth.api.UserAccountCreationDTO;
import com.marketplace.Auth.api.UserMapper;
import com.marketplace.Auth.domain.Role;
import com.marketplace.Auth.domain.User;
import com.marketplace.Auth.domain.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @InjectMocks
    private UserService userService;

    @Autowired
    private UserMapper userMapper;
    private User user;
    private String email;
    private String name;
    private String password;
    private String repeatPassword;
    private Role role;

    @BeforeEach
    protected void setup() {
        email = "email_test";
        name = "name_test";
        password = "password_test";
        repeatPassword = "password_test";
        role = new Role(Role.RoleEnum.BUYER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user = new User(email, name, password, roles);
    }

    @RepeatedTest(3)
    public void createUserServiceTest_returnUser() {
        UserAccountCreationDTO userDto = new UserAccountCreationDTO(email, name, password, repeatPassword);
        User savedUser = userService.createUserAccount(role, userMapper, userDto);
        assertThat(savedUser.getEmail()).isEqualTo(email);
        assertThat(savedUser.getName()).isEqualTo(name);
        assertThat(savedUser.getAccountRoles()).isNotNull();
        assertThat(savedUser.getPassword()).isNotNull();

    }
}
