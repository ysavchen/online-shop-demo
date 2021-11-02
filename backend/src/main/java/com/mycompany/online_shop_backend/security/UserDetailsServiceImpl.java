package com.mycompany.online_shop_backend.security;

import com.mycompany.online_shop_backend.exceptions.NotAuthorizedException;
import com.mycompany.online_shop_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String ROLE_USER = "ROLE_USER";
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));

        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(ROLE_USER)
                .build();
    }
}
