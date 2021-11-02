package com.mycompany.online_shop_backend.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final TokenService tokenService;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;

    public Authentication authenticate(String email, String password) {
        return authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public String getUsernameFromRequest(HttpServletRequest request) {
        String token = tokenService.detachToken(request);
        return tokenService.getUsernameFromToken(token);
    }
}
