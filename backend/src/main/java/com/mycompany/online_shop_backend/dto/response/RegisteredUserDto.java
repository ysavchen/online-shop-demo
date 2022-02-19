package com.mycompany.online_shop_backend.dto.response;

import com.mycompany.online_shop_backend.dto.services.UserDto;

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
