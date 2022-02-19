package com.mycompany.online_shop_backend.dto.response;

public record RegisterResponse(
        String token,
        long tokenExpiration,
        RegisteredUserDto registeredUser
) { }
