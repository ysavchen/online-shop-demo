package com.mycompany.online_shop_backend.controllers;

import com.mycompany.online_shop_backend.dto.services.BookDto;
import com.mycompany.online_shop_backend.dto.response.GetBookByIdResponseDto;
import com.mycompany.online_shop_backend.dto.response.GetBooksResponseDto;
import com.mycompany.online_shop_backend.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping(
            path = "/v1/books",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<GetBooksResponseDto> getBooks() {
        return bookService.getAllBooks()
                .stream()
                .map(GetBooksResponseDto::toDto)
                .toList();
    }

    @GetMapping(
            path = "/v1/books/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public GetBookByIdResponseDto getBookById(@PathVariable("id") long id) {
        BookDto bookDto = bookService.getById(id);
        return GetBookByIdResponseDto.toDto(bookDto);
    }
}
