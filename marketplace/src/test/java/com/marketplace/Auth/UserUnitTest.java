package com.marketplace.Auth;

import com.marketplace.Auth.api.UserAccountCreationDTO;
import com.marketplace.Auth.api.UserMapper;
import com.marketplace.Auth.domain.Role;
import com.marketplace.Auth.domain.User;
import com.marketplace.Auth.domain.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserUnitTest {

    @InjectMocks
    private UserService userService;

    @Autowired
    private UserMapper userMapper;
    private User user;
    private Role role;

    private String email;
    private String name;
    private String password;
    private String repeatPassword;

    @BeforeEach
    protected void setup() {

        email = "email_test";
        name = "name_test";
        password = "password_test";
        repeatPassword = "password_test";

        Set<Role> roles = new HashSet<>();
        role = new Role(Role.RoleEnum.BUYER);
        roles.add(role);
        user = new User(email, name, password, roles);

    }

    @RepeatedTest(3)
    public void saveUser_andReturnUser() {
        UserAccountCreationDTO userAuthDto = new UserAccountCreationDTO(email, name, password, repeatPassword);
        User user1 = userService.createUserAccount(role, userMapper, userAuthDto);

        assertThat(user1.getName()).isEqualTo(name);
        assertThat(user1.getEmail()).isEqualTo(email);
    }

    @RepeatedTest(3)
    public void checkUserByEmailTest_andReturnFalse() {

        boolean isUserHasAlreadyRegistered = userService.checkUserByEmail(email);
        assertThat(isUserHasAlreadyRegistered).isFalse();
    }
}
