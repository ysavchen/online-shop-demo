package com.mycompany.online_shop_backend.dto.response;

import com.mycompany.online_shop_backend.dto.services.UserDto;
import lombok.Data;

@Data
public class RegisteredUserDto {

    private final long id;
    private final String firstName;
    private final String lastName;
    private final String email;

    public static RegisteredUserDto toDto(UserDto user) {
        return new RegisteredUserDto(
                user.id(),
                user.firstName(),
                user.lastName(),
                user.email()
        );
    }
}
