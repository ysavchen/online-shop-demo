package com.mycompany.online_shop_backend.services;

import com.mycompany.online_shop_backend.repositories.domain.User;
import com.mycompany.online_shop_backend.dto.services.UserDto;
import com.mycompany.online_shop_backend.dto.request.RegisterRequest;
import com.mycompany.online_shop_backend.exceptions.EntityNotFoundException;
import com.mycompany.online_shop_backend.repositories.UserRepository;
import com.mycompany.online_shop_backend.services.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SecurityService securityService;

    @Transactional
    public UserDto register(RegisterRequest request) {
        User user = RegisterRequest.toUserEntity(request);
        user.setPassword(securityService.encodePassword(user.getPassword()));
        return UserDto.toDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserDto::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User with email = " + email + " is not found"));
    }
}
