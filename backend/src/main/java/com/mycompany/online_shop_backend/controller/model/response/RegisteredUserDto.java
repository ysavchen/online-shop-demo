package com.mycompany.online_shop_backend.controller.model.response;

import com.mycompany.online_shop_backend.service.model.UserDto;

public record RegisteredUserDto(
        long id,
        String firstName,
        String lastName,
        String email
) {
    public static RegisteredUserDto toDto(UserDto user) {
        return new RegisteredUserDto(
                user.id(),
                user.firstName(),
                user.lastName(),
                user.email()
        );
    }
}
