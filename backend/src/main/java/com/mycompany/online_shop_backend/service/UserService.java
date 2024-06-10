package com.mycompany.online_shop_backend.service;

import com.mycompany.online_shop_backend.repository.domain.User;
import com.mycompany.online_shop_backend.service.model.UserDto;
import com.mycompany.online_shop_backend.controller.model.request.RegisterRequest;
import com.mycompany.online_shop_backend.exception.EntityNotFoundException;
import com.mycompany.online_shop_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto register(RegisterRequest request) {
        User user = RegisterRequest.toUserEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return UserDto.toDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserDto::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User with email = " + email + " is not found"));
    }
}
