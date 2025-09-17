package com.marketplace.Auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.Auth.api.AuthController;
import com.marketplace.Auth.api.UserAccountCreationDTO;
import com.marketplace.Auth.api.UserMapper;
import com.marketplace.Auth.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {AuthController.class, AuthControllerTest.TestConfig.class})
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private RoleService roleService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public JwtService jwtService() {
            return org.mockito.Mockito.mock(JwtService.class);
        }
    }

    private Role role;
    private User user;
    private String email;
    private String name;
    private String password;
    private String repeatPassword;

    @BeforeEach
    void setup() {
        Set<Role> roles = new HashSet<>();
        role = new Role(Role.RoleEnum.BUYER);
        roles.add(role);

        email = "email@email_test.com";
        name = "name_test";
        password = "ssssss123";
        repeatPassword = "ssssss123";
        user = new User(email, name, password, roles);
    }

    @RepeatedTest(3)
    public void createUser_andReturnResponseOK() throws Exception {
        UserAccountCreationDTO userDto = new UserAccountCreationDTO(email, name, password, repeatPassword);
        given(userService.checkUserByEmail(email)).willReturn(false);
        given(roleService.getOrCreateRoleAccount(Role.RoleEnum.BUYER)).willReturn(role);
        Role role1 = roleService.getOrCreateRoleAccount(Role.RoleEnum.BUYER);
        System.out.println("role1 = " + role1);
        assertThat(role1.getRoleName()).isEqualTo(Role.RoleEnum.BUYER);
        given(userService.createUserAccount(role1, userMapper, userDto)).willReturn(user);
        User user1 = userService.createUserAccount(role1, userMapper, userDto);
        System.out.println("user1 = " + user1);
//        ResultActions response = mockMvc.perform(post("http://localhost:8080/user/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(userDto))
//        );
        ResultActions response = mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto))
        );

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Register Successfully"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"));

    }
}
