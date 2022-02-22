package com.mycompany.online_shop_backend.dto.request;

import com.mycompany.online_shop_backend.repositories.domain.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {
    public static User toUserEntity(RegisterRequest dto) {
        return new User(0L, dto.firstName, dto.lastName, dto.email, dto.password, Set.of(new SimpleGrantedAuthority("USER")));
    }
}
