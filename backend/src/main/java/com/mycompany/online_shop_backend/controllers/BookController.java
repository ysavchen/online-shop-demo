package com.mycompany.online_shop_backend.controllers;

import com.mycompany.online_shop_backend.dto.services.BookDto;
import com.mycompany.online_shop_backend.dto.response.GetBookByIdResponseDto;
import com.mycompany.online_shop_backend.dto.response.GetBooksResponseDto;
import com.mycompany.online_shop_backend.services.BookService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation("Gets all books")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful execution")
    })
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

    @ApiOperation("Gets a book with id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Book is found"),
            @ApiResponse(code = 404, message = "Book is not found")
    })
    @GetMapping(
            path = "/v1/books/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public GetBookByIdResponseDto getBookById(@PathVariable("id") long id) {
        BookDto bookDto = bookService.getById(id);
        return GetBookByIdResponseDto.toDto(bookDto);
    }
}
