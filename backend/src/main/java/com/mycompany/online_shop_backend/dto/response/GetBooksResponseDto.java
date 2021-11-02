package com.mycompany.online_shop_backend.dto.response;

import com.mycompany.online_shop_backend.dto.services.BookDto;
import lombok.Data;

@Data
public class GetBooksResponseDto {

    private final long id;
    private final String title;
    private final String description;
    private final String author;
    private final String image;
    private final double price;

    public static GetBooksResponseDto toDto(BookDto book) {
        return new GetBooksResponseDto(
                book.id(),
                book.title(),
                book.description(),
                book.author(),
                book.image(),
                book.price()
        );
    }

}
