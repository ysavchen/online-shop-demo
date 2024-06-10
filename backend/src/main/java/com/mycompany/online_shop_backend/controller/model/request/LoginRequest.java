package com.mycompany.online_shop_backend.controller.model.request;

public record LoginRequest(
        String email,
        String password
) { }
