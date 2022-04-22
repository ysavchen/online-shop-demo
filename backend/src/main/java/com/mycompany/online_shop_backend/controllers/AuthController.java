package com.mycompany.online_shop_backend.controllers;

import com.mycompany.online_shop_backend.dto.response.LoggedInUserDto;
import com.mycompany.online_shop_backend.dto.response.RegisteredUserDto;
import com.mycompany.online_shop_backend.dto.services.UserDto;
import com.mycompany.online_shop_backend.dto.request.LoginRequest;
import com.mycompany.online_shop_backend.dto.request.RegisterRequest;
import com.mycompany.online_shop_backend.dto.response.LoginResponse;
import com.mycompany.online_shop_backend.dto.response.RegisterResponse;
import com.mycompany.online_shop_backend.services.UserService;
import com.mycompany.online_shop_backend.services.SecurityService;
import com.mycompany.online_shop_backend.services.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final SecurityService securityService;
    private final TokenService tokenService;

    @PostMapping(
            path = "/v1/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        log.info("Register user with email {}", request.email());

        UserDto userDto = userService.register(request);
        String token = tokenService.generateToken(userDto.email());
        long tokenExpiration = tokenService.getTokenExpiration();

        return new RegisterResponse(token, tokenExpiration, RegisteredUserDto.toDto(userDto));
    }

    @PostMapping(
            path = "/v1/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public LoginResponse login(@RequestBody LoginRequest request) {
        log.info("Login with email {}", request.email());
        securityService.authenticate(request.email(), request.password());

        UserDto userDto = userService.findByEmail(request.email());
        String token = tokenService.generateToken(userDto.email());
        long tokenExpiration = tokenService.getTokenExpiration();

        return new LoginResponse(token, tokenExpiration, LoggedInUserDto.toDto(userDto));
    }
}
