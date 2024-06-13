package com.mycompany.online_shop_backend.controller;

import com.mycompany.online_shop_backend.controller.model.response.GetBookByIdResponseDto;
import com.mycompany.online_shop_backend.controller.model.response.GetBooksResponseDto;
import com.mycompany.online_shop_backend.service.BookService;
import com.mycompany.online_shop_backend.service.model.BookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController {

    private final BookService bookService;

    @GetMapping(path = "/api/v1/books")
    public List<GetBooksResponseDto> getBooks() {
        return bookService.getAllBooks()
                .stream()
                .map(GetBooksResponseDto::toDto)
                .toList();
    }

    @GetMapping(path = "/api/v1/books/{id}")
    public GetBookByIdResponseDto getBookById(@PathVariable("id") long id) {
        BookDto bookDto = bookService.getById(id);
        return GetBookByIdResponseDto.toDto(bookDto);
    }
}
