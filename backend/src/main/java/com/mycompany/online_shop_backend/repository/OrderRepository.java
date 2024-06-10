package com.mycompany.online_shop_backend.repository;

import com.mycompany.online_shop_backend.repository.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByEmail(String email);
}
