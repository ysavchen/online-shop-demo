package com.mycompany.online_shop_backend.dto.response;

import com.mycompany.online_shop_backend.dto.services.BookDto;
import com.mycompany.online_shop_backend.dto.services.OrderDto;

import java.util.List;

public record CreatedOrderResponse(
        long id,
        String name,
        String address,
        String phone,
        String email,
        String createdAt,
        List<BookDto> books
) {
    public static CreatedOrderResponse toDto(OrderDto order) {
        return new CreatedOrderResponse(
                order.id(),
                order.name(),
                order.address(),
                order.phone(),
                order.email(),
                order.createdAt(),
                order.books()
        );
    }
}
