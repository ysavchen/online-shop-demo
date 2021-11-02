package com.mycompany.online_shop_backend.security;

import com.mycompany.online_shop_backend.exceptions.NotAuthorizedException;
import com.mycompany.online_shop_backend.services.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
//todo: extend AuthenticationFilter or AbstractAuthenticationProcessingFilter???
public class TokenAuthenticationFilter extends GenericFilterBean {

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("Entering token filter");
        if (!(request instanceof HttpServletRequest)) {
            throw new NotAuthorizedException("Non supported request");
        }
        String token = tokenService.detachToken((HttpServletRequest) request);
        if (token != null && tokenService.validateToken(token)) {
            Authentication auth = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(tokenService.getUsernameFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }
}
