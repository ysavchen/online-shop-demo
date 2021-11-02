package com.mycompany.online_shop_backend.controllers;

import com.google.gson.Gson;
import com.mycompany.online_shop_backend.domain.Author;
import com.mycompany.online_shop_backend.domain.Book;
import com.mycompany.online_shop_backend.dto.response.GetBookByIdResponseDto;
import com.mycompany.online_shop_backend.dto.response.GetBooksResponseDto;
import com.mycompany.online_shop_backend.dto.services.BookDto;
import com.mycompany.online_shop_backend.exceptions.EntityNotFoundException;
import com.mycompany.online_shop_backend.repositories.BookRepository;
import com.mycompany.online_shop_backend.repositories.UserRepository;
import com.mycompany.online_shop_backend.security.SecurityConfiguration;
import com.mycompany.online_shop_backend.security.TokenAuthenticationFilter;
import com.mycompany.online_shop_backend.security.TokenProperties;
import com.mycompany.online_shop_backend.security.UserDetailsServiceImpl;
import com.mycompany.online_shop_backend.services.BookService;
import com.mycompany.online_shop_backend.services.security.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import({TokenAuthenticationFilter.class,
        TokenProperties.class,
        TokenService.class,
        SecurityConfiguration.class,
        UserDetailsServiceImpl.class,
        BookRepository.class,
        UserRepository.class})
public class BookControllerTests {

    private final Author authorOne = new Author(1L, "Author One");
    private final Author authorTwo = new Author(2L, "Author Two");

    private final Book bookOne = new Book(
            1L,
            "Book One",
            "Description One",
            authorOne,
            "/imageOne",
            22.95
    );
    private final Book bookTwo = new Book(
            2L,
            "Book Two",
            "Description Two",
            authorTwo,
            "/imageTwo",
            46.00
    );

    private final BookDto bookDtoOne = BookDto.toDto(bookOne);
    private final BookDto bookDtoTwo = BookDto.toDto(bookTwo);
    private final GetBooksResponseDto getBooksResponseDtoOne = GetBooksResponseDto.toDto(bookDtoOne);
    private final GetBooksResponseDto getBooksResponseDtoTwo = GetBooksResponseDto.toDto(bookDtoTwo);
    private final List<BookDto> bookDtos = List.of(bookDtoOne, bookDtoTwo);
    private final List<GetBooksResponseDto> getBooksDtos = List.of(getBooksResponseDtoOne, getBooksResponseDtoTwo);
    private final GetBookByIdResponseDto getBookByIdResponseDto = GetBookByIdResponseDto.toDto(bookDtoOne);
    private static final long NON_EXISTING_ID = 50L;

    private final Gson gson = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void getBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(bookDtos);
        mockMvc.perform(get("/v1/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(getBooksDtos)));
    }

    @Test
    public void getBookById() throws Exception {
        when(bookService.getById(bookOne.getId())).thenReturn(bookDtoOne);
        mockMvc.perform(get("/v1/books/{id}", getBookByIdResponseDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(getBookByIdResponseDto)));
    }

    @Test
    public void getBookByIdNegative() throws Exception {
        when(bookService.getById(NON_EXISTING_ID))
                .thenThrow(new EntityNotFoundException("Book with id = " + NON_EXISTING_ID + " is not found"));
        mockMvc.perform(get("/v1/books/{id}", NON_EXISTING_ID))
                .andExpect(status().isNotFound());
    }
}
