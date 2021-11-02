package com.mycompany.online_shop_backend.dto.request;

import com.mycompany.online_shop_backend.domain.User;
import lombok.Data;

@Data
public class RegisterRequest {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;

    public static User toUserEntity(RegisterRequest dto) {
        return new User(0L, dto.firstName, dto.lastName, dto.email, dto.password);
    }
}
