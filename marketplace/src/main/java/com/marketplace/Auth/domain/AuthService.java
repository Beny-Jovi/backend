package com.marketplace.Auth.domain;

import com.marketplace.Auth.api.TokenDto;
import com.marketplace.Auth.api.UserLoginDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

//@Service
//@AllArgsConstructor
//public class AuthService {
//
//    private final AuthenticationManager authenticationManager;
//
//    @Autowired
//    JwtService jwtService;
//    @Autowired
//    UserService userService;
//
//    public TokenDto authenticateUser(UserLoginDto userLoginDto) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        userLoginDto.email(),
//                        userLoginDto.password()
//                )
//        );
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String token = jwtService.generateToken(userDetails);
//        String refreshToken = jwtService.refreshToken(userDetails);
//        return new TokenDto(token, refreshToken);
//    }
//
//    public TokenDto generateRefreshToken(String refreshTokenCookie) {
//        String email = jwtService.getUsernameFromRefreshToken(refreshTokenCookie);
//        User user = userService.getUserByEmail(email);
//        String token = jwtService.generateToken((UserDetails) user);
//        String refreshToken = jwtService.refreshToken((UserDetails) user);
//        return new TokenDto(token, refreshToken);
//    }
//}
