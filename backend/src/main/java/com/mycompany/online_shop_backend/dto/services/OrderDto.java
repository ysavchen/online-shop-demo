package com.mycompany.online_shop_backend.dto.services;

import com.mycompany.online_shop_backend.domain.Order;
import com.mycompany.online_shop_backend.domain.OrderBook;

import java.time.ZoneOffset;
import java.util.List;

public record OrderDto(
        long id,
        String name,
        String address,
        String phone,
        String email,
        String createdAt,
        List<BookDto> books
) {
    public static OrderDto toDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getAddresseeName(),
                order.getAddress().getValue(),
                order.getPhone().getValue(),
                order.getEmail(),
                order.getCreatedAt().toInstant(ZoneOffset.UTC).toString(),
                order.getOrderBooks().stream()
                        .map(OrderBook::getBook)
                        .map(BookDto::toDto)
                        .toList()
        );
    }
}
