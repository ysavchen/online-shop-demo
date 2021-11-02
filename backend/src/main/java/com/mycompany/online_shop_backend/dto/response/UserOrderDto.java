package com.mycompany.online_shop_backend.dto.response;

import com.mycompany.online_shop_backend.dto.services.BookDto;
import com.mycompany.online_shop_backend.dto.services.OrderDto;
import lombok.Data;

import java.util.List;

@Data
public class UserOrderDto {
    private final long id;
    private final String name;
    private final String address;
    private final String phone;
    private final String email;
    private final String createdAt;
    private final List<BookDto> books;

    public static UserOrderDto toDto(OrderDto order) {
        return new UserOrderDto(
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
