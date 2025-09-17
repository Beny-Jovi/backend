package com.marketplace.Auth.api;

import com.marketplace.Auth.domain.*;
import com.marketplace.Exception.ResourceDuplicationException;
import com.marketplace.Util.ResponseHandler;
import de.huxhorn.sulky.ulid.ULID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@AllArgsConstructor
@RequestMapping("/user")
@RestController
public class AuthController {

    private final UserService userService;
    private final UserMapper mapper;
    private final RoleService roleService;
//    private final AuthService authService;

    @Operation(summary = "User can be register in here")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully to create users",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountDto.class)) }
            ),
            @ApiResponse(responseCode = "302", description = "the email has already been taken",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Email already registered\", \"cause\": \"Duplicate resource\"}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid inserted value",
                    content = { @Content(mediaType = "application/json", schema = @Schema(
                            example = "Invalid inserted value"
                    )) }
            )
    })
    @PostMapping("/register")
    public ResponseEntity<Object> createAccount(@RequestBody @Valid UserAccountCreationDTO accountDTO) {
        if (!accountDTO.repeatPassword().equals(accountDTO.password())) {
            throw new IllegalArgumentException("repeat password should match the password");
        }
        Boolean isUserRegistered = userService.checkUserByEmail(accountDTO.email());
        if (isUserRegistered) {
            throw new ResourceDuplicationException("the name of the account has already been taken");
        }
        Role role = roleService.getOrCreateRoleAccount(Role.RoleEnum.BUYER);
        System.out.println("role in controller= " + role);
        userService.createUserAccount(role, mapper, accountDTO);
        return ResponseHandler.generateResponse("Register Successfully", HttpStatus.CREATED, "");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> signIn(@RequestBody @Valid UserLoginDto userLoginDto, @CookieValue(value = "token", required = false) String token, HttpServletResponse response) {
        if (token != null) {
            return ResponseEntity.ok("You are already logged in");
        }
//        TokenDto authenticatedUser = authService.authenticateUser(userLoginDto);
//        String jwtToken = jwtService.generateToken(authenticatedUser);

//        Cookie tokenCookie = new Cookie("token", authenticatedUser.userToken());

//        Adjust cookie later in production
//        tokenCookie.setMaxAge(3600);
//        tokenCookie.setSecure(true);
//        tokenCookie.setPath("/");
//        response.addCookie(tokenCookie);

//        Cookie refreshTokenCookie = new Cookie("refresh_token", authenticatedUser.refreshToken());
//        refreshTokenCookie.setMaxAge(360000);
//        refreshTokenCookie.setSecure(true);
//        refreshTokenCookie.setPath("/");
//        response.addCookie(refreshTokenCookie);

        return ResponseHandler.generateResponse("Login Successfully", HttpStatus.CREATED, "");
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Object> userLogout(@CookieValue(value = "refresh_token", required = false) String token, HttpServletResponse response) {
        if (token == null) {
            return ResponseEntity.ok("You are not logged in");
        }
        Cookie tokenCookie = new Cookie("token", "");

//        Adjust cookie later in production
        tokenCookie.setMaxAge(0);
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);

        Cookie refreshTokenCookie = new Cookie("refresh_token", "");
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);
//        return ResponseHandler.generateResponse("", HttpStatus.NO_CONTENT, "");
        return ResponseEntity.ok("");
    }

    @GetMapping("/refresh/refresh_token")
    public ResponseEntity<Object> refreshToken(@RequestParam("refresh_token") String refreshToken, HttpServletResponse response) {
//        TokenDto authenticatedUser = authService.generateRefreshToken(refreshToken);

//        Cookie tokenCookie = new Cookie("token", authenticatedUser.userToken());

//        Adjust cookie later in production
//        tokenCookie.setMaxAge(36000);
//        tokenCookie.setSecure(true);
//        tokenCookie.setPath("/");
//        response.addCookie(tokenCookie);

//        Cookie refreshTokenCookie = new Cookie("refresh_token", authenticatedUser.refreshToken());
//        refreshTokenCookie.setMaxAge(360000);
//        refreshTokenCookie.setSecure(true);
//        refreshTokenCookie.setPath("/");
//        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok("");
    }

//    @GetMapping("/csrf_token")
//    public CsrfToken getCsrfToken(CsrfToken csrfToken) {
//        return csrfToken;
//        String csrfToken = new ULID().nextULID();
//        System.out.println("csrfToken = " + csrfToken);
//        Cookie tokenCookie = new Cookie("CSRF_TOKEN", csrfToken);
////        Adjust cookie later in production
//        tokenCookie.setMaxAge(-1);
//        tokenCookie.setHttpOnly(false);
//        tokenCookie.setPath("/");
//        tokenCookie.setSecure(true);
//        response.addCookie(tokenCookie);
//        System.out.println("httpSession = " + httpSession);
//        return ResponseEntity.ok(csrfToken);
//    }

//    @GetMapping("/csrf_token")
//    public CsrfToken getCsrfToken(HttpServletRequest request) {
//        return (CsrfToken) request.getAttribute("_csrf");
//    }

}