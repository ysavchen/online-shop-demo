package com.mycompany.online_shop_backend.controller.model.request;

import com.mycompany.online_shop_backend.repository.domain.User;

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
