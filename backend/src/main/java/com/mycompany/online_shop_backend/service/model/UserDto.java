package com.mycompany.online_shop_backend.service.model;

import com.mycompany.online_shop_backend.repository.domain.User;

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
