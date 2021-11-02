package com.mycompany.online_shop_backend.dto.response;

import com.mycompany.online_shop_backend.dto.services.UserDto;
import lombok.Data;

@Data
public class LoggedInUserDto {

    private final long id;
    private final String firstName;
    private final String lastName;
    private final String email;

    public static LoggedInUserDto toDto(UserDto user) {
        return new LoggedInUserDto(
                user.id(),
                user.firstName(),
                user.lastName(),
                user.email()
        );
    }
}
