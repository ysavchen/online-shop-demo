package com.mycompany.online_shop_backend.services.security;

import com.mycompany.online_shop_backend.security.TokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenService {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";

    private final TokenProperties tokenProperties;

    public String generateToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        Date now = Date.from(Instant.now());
        Date expiration = new Date(now.getTime() + tokenProperties.getExpiration());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, tokenProperties.getSecretKey())
                .compact();
    }

    public long getTokenExpiration() {
        return tokenProperties.getExpiration();
    }

    public boolean validateToken(String token) {
        Date now = Date.from(Instant.now());
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(tokenProperties.getSecretKey())
                .parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(now);
    }

    public String detachToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }
        return null;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(tokenProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
