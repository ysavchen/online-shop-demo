package com.mycompany.online_shop_backend.dto.request;

import com.mycompany.online_shop_backend.repositories.domain.User;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {
    public static User toUserEntity(RegisterRequest dto) {
        return new User(0L, dto.firstName, dto.lastName, dto.email, dto.password);
    }
}
