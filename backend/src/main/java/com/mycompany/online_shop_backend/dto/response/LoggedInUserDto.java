package com.mycompany.online_shop_backend.dto.response;

import com.mycompany.online_shop_backend.dto.services.UserDto;

public record LoggedInUserDto(
        long id,
        String firstName,
        String lastName,
        String email
) {
    public static LoggedInUserDto toDto(UserDto user) {
        return new LoggedInUserDto(
                user.id(),
                user.firstName(),
                user.lastName(),
                user.email()
        );
    }
}
