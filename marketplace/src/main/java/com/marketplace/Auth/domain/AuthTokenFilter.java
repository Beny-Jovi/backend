package com.marketplace.Auth.domain;

import com.marketplace.RateLimiter.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import io.jsonwebtoken.security.SignatureException;

@Component
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    private HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    private RateLimiterService rateLimiterService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        System.out.println("authHeader = " + authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String token = authHeader.substring(7);
            final String userEmail = jwtService.getUsernameFromToken(token);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
                if (!rateLimiterService.isAllowed(userEmail)) {
                    throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
//                    filterChain.doFilter(request, response);
//                    return;
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            log.error("error in do filter internal: {}", exception.getMessage());
            log.error("error cause is: {}", exception.getCause());
            System.out.println("error message is: " +exception.getMessage());

            if (exception instanceof ResponseStatusException rse) {
                // Optionally customize here, e.g., set specific headers
                response.setStatus(rse.getStatusCode().value());
                assert rse.getReason() != null;
                response.getWriter().write(rse.getReason());
                response.getWriter().flush();
                return;  // Prevent further propagation or resolution
            }

            handlerExceptionResolver.resolveException(request, response, null, exception);

        }
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
