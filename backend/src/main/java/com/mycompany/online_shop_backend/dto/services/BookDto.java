package com.mycompany.online_shop_backend.dto.services;

import com.mycompany.online_shop_backend.domain.Author;
import com.mycompany.online_shop_backend.domain.Book;

public record BookDto(
        long id,
        String title,
        String description,
        String author,
        String image,
        double price
) {
    public static BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getDescription(),
                book.getAuthor().getFullName(),
                book.getImage(),
                book.getPrice()
        );
    }

    public static Book toEntity(BookDto dto) {
        return new Book(
                dto.id, dto.title,
                dto.description,
                new Author(0L, dto.author),
                dto.image,
                dto.price
        );
    }
}
