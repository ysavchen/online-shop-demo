package com.mycompany.online_shop_backend.controllers;

import com.google.gson.Gson;
import com.mycompany.online_shop_backend.domain.User;
import com.mycompany.online_shop_backend.dto.response.*;
import com.mycompany.online_shop_backend.dto.services.UserDto;
import com.mycompany.online_shop_backend.dto.request.LoginRequest;
import com.mycompany.online_shop_backend.dto.request.RegisterRequest;
import com.mycompany.online_shop_backend.exceptions.NotAuthorizedException;
import com.mycompany.online_shop_backend.repositories.UserRepository;
import com.mycompany.online_shop_backend.security.SecurityConfiguration;
import com.mycompany.online_shop_backend.security.TokenAuthenticationFilter;
import com.mycompany.online_shop_backend.security.TokenProperties;
import com.mycompany.online_shop_backend.security.UserDetailsServiceImpl;
import com.mycompany.online_shop_backend.services.UserService;
import com.mycompany.online_shop_backend.services.security.SecurityService;
import com.mycompany.online_shop_backend.services.security.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({TokenAuthenticationFilter.class,
        TokenProperties.class,
        TokenService.class,
        SecurityConfiguration.class,
        UserDetailsServiceImpl.class,
        UserRepository.class})
public class AuthControllerTests {

    private final String userOneEmail = "userOne@test.com";
    private final String userOnePassword = "Start01#";
    private final String userOnePasswordEncoded = "Encoded Start01#";
    private final String token = "Secret token";
    private final long tokenExpiration = 10000L;

    private final User userOne = new User(
            1L,
            "Name One",
            "Surname One",
            userOneEmail,
            userOnePasswordEncoded
    );
    private final UserDto userOneDto = UserDto.toDto(userOne);
    private final RegisteredUserDto registeredUserDtoOne = RegisteredUserDto.toDto(userOneDto);
    private final LoggedInUserDto loggedInUserDto = LoggedInUserDto.toDto(userOneDto);

    private final RegisterRequest registerRequest = new RegisterRequest(
            userOneDto.firstName(),
            userOneDto.lastName(),
            userOneEmail,
            userOnePassword
    );
    private final RegisterResponse registerResponse = new RegisterResponse(token, tokenExpiration, registeredUserDtoOne);
    private final LoginResponse loginResponse = new LoginResponse(token, tokenExpiration, loggedInUserDto);
    private final LoginRequest loginRequest = new LoginRequest(userOneEmail, userOnePassword);

    private final Gson gson = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void register() throws Exception {
        when(tokenService.generateToken(anyString())).thenReturn(token);
        when(tokenService.getTokenExpiration()).thenReturn(tokenExpiration);
        when(userService.register(any(RegisterRequest.class))).thenReturn(userOneDto);

        mockMvc.perform(
                post("/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(gson.toJson(registerResponse)));
    }

    @Test
    public void login() throws Exception {
        when(securityService.authenticate(anyString(), anyString()))
                .thenReturn(new UsernamePasswordAuthenticationToken(userOneEmail, userOnePassword));
        when(userService.findByEmail(registerRequest.getEmail())).thenReturn(userOneDto);
        when(tokenService.generateToken(userOneDto.email())).thenReturn(token);
        when(tokenService.getTokenExpiration()).thenReturn(tokenExpiration);

        mockMvc.perform(
                post("/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(gson.toJson(loginResponse)));
    }

    @Test
    public void loginWithFailedAuthentication() throws Exception {
        when(securityService.authenticate(anyString(), anyString()))
                .thenThrow(new NotAuthorizedException("Not authorized"));

        mockMvc.perform(
                post("/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}
