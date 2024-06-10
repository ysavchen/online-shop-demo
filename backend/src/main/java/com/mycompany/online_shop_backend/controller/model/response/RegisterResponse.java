package com.mycompany.online_shop_backend.controller.model.response;

public record RegisterResponse(
        String token,
        long tokenExpiration,
        RegisteredUserDto registeredUser
) { }
