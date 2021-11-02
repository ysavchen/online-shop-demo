package com.mycompany.online_shop_backend.dto.response;

import lombok.Data;

@Data
public class RegisterResponse {

    private final String token;
    private final long tokenExpiration;
    private final RegisteredUserDto registeredUser;

}
