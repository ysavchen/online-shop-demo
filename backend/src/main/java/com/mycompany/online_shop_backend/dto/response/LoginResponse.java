package com.mycompany.online_shop_backend.dto.response;

public record LoginResponse(
        String token,
        long tokenExpiration,
        LoggedInUserDto user
) { }
