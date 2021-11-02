package com.mycompany.online_shop_backend.dto.request;

import com.mycompany.online_shop_backend.domain.Address;
import com.mycompany.online_shop_backend.domain.Order;
import com.mycompany.online_shop_backend.domain.Phone;
import com.mycompany.online_shop_backend.dto.services.BookDto;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    private final String name;
    private final String address;
    private final String phone;
    private final String email;
    private final List<BookDto> books;

    public static Order toEntity(OrderRequest dto) {
        Order order = new Order();
        order.setId(0L);
        order.setAddresseeName(dto.getName());
        order.setAddress(new Address(0L, dto.getAddress()));
        order.setPhone(new Phone(0L, dto.getPhone()));
        order.setEmail(dto.getEmail());

        dto.books.stream()
                .map(BookDto::toEntity)
                .forEach(order::addBook);

        return order;
    }
}
