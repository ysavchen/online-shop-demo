package com.mycompany.online_shop_backend.controller.model.request;

import com.mycompany.online_shop_backend.repository.domain.Address;
import com.mycompany.online_shop_backend.repository.domain.Order;
import com.mycompany.online_shop_backend.repository.domain.Phone;
import com.mycompany.online_shop_backend.service.model.BookDto;

import java.util.List;

public record OrderRequest(
        String name,
        String address,
        String phone,
        String email,
        List<BookDto> books
) {
    public static Order toEntity(OrderRequest dto) {
        Order order = new Order();
        order.setId(0L);
        order.setAddresseeName(dto.name());
        order.setAddress(new Address(0L, dto.address()));
        order.setPhone(new Phone(0L, dto.phone()));
        order.setEmail(dto.email());

        dto.books.stream()
                .map(BookDto::toEntity)
                .forEach(order::addBook);

        return order;
    }
}
