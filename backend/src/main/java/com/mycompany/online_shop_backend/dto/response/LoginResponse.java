package com.mycompany.online_shop_backend.dto.response;

import lombok.Data;

@Data
public class LoginResponse {

    private final String token;
    private final long tokenExpiration;
    private final LoggedInUserDto user;

}
