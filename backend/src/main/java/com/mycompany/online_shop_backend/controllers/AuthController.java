package com.mycompany.online_shop_backend.controllers;

import com.mycompany.online_shop_backend.dto.response.LoggedInUserDto;
import com.mycompany.online_shop_backend.dto.response.RegisteredUserDto;
import com.mycompany.online_shop_backend.dto.services.UserDto;
import com.mycompany.online_shop_backend.dto.request.LoginRequest;
import com.mycompany.online_shop_backend.dto.request.RegisterRequest;
import com.mycompany.online_shop_backend.dto.response.LoginResponse;
import com.mycompany.online_shop_backend.dto.response.RegisterResponse;
import com.mycompany.online_shop_backend.services.UserService;
import com.mycompany.online_shop_backend.services.security.SecurityService;
import com.mycompany.online_shop_backend.services.security.TokenService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation("Registers a user")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successful registration")
    })
    @PostMapping(
            path = "/v1/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        log.info("Register user with email {}", request.getEmail());

        UserDto userDto = userService.register(request);
        String token = tokenService.generateToken(userDto.email());
        long tokenExpiration = tokenService.getTokenExpiration();

        return new RegisterResponse(token, tokenExpiration, RegisteredUserDto.toDto(userDto));
    }

    @ApiOperation("Logs in a user")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful login"),
            @ApiResponse(code = 404, message = "Resource not found")
    })
    @PostMapping(
            path = "/v1/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public LoginResponse login(@RequestBody LoginRequest request) {
        log.info("Login with email {}", request.getEmail());
        securityService.authenticate(request.getEmail(), request.getPassword());

        UserDto userDto = userService.findByEmail(request.getEmail());
        String token = tokenService.generateToken(userDto.email());
        long tokenExpiration = tokenService.getTokenExpiration();

        return new LoginResponse(token, tokenExpiration, LoggedInUserDto.toDto(userDto));
    }
}
