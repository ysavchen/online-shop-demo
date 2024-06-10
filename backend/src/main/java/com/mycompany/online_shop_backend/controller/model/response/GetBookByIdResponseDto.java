package com.mycompany.online_shop_backend.controller.model.response;

import com.mycompany.online_shop_backend.service.model.BookDto;

public record GetBookByIdResponseDto(
        long id,
        String title,
        String description,
        String author,
        String image,
        double price
) {
    public static GetBookByIdResponseDto toDto(BookDto book) {
        return new GetBookByIdResponseDto(
                book.id(),
                book.title(),
                book.description(),
                book.author(),
                book.image(),
                book.price()
        );
    }

}
