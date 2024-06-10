package com.mycompany.online_shop_backend.controller.model.response;

public record LoginResponse(
        String token,
        long tokenExpiration,
        LoggedInUserDto user
) { }
