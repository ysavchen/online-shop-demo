package com.mycompany.online_shop_backend.dto.request;

public record LoginRequest(
        String email,
        String password
) { }
