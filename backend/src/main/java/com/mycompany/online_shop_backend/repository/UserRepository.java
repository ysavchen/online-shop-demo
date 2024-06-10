package com.mycompany.online_shop_backend.repository;

import com.mycompany.online_shop_backend.repository.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
