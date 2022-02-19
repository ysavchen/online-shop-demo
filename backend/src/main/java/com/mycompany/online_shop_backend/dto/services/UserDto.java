package com.mycompany.online_shop_backend.dto.services;

import com.mycompany.online_shop_backend.repositories.domain.User;

public record UserDto(
        long id,
        String firstName,
        String lastName,
        String email
) {
    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}
