package com.mycompany.online_shop_backend.controller;

import com.mycompany.online_shop_backend.service.model.BookDto;
import com.mycompany.online_shop_backend.controller.model.response.GetBookByIdResponseDto;
import com.mycompany.online_shop_backend.controller.model.response.GetBooksResponseDto;
import com.mycompany.online_shop_backend.service.BookService;
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
            path = "/api/v1/books",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<GetBooksResponseDto> getBooks() {
        return bookService.getAllBooks()
                .stream()
                .map(GetBooksResponseDto::toDto)
                .toList();
    }

    @GetMapping(
            path = "/api/v1/books/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public GetBookByIdResponseDto getBookById(@PathVariable("id") long id) {
        BookDto bookDto = bookService.getById(id);
        return GetBookByIdResponseDto.toDto(bookDto);
    }
}
