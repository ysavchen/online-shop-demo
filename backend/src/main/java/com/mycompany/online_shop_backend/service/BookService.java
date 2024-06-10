package com.mycompany.online_shop_backend.service;

import com.mycompany.online_shop_backend.exception.EntityNotFoundException;
import com.mycompany.online_shop_backend.repository.BookRepository;
import com.mycompany.online_shop_backend.service.model.BookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public BookDto getById(long id) {
        return bookRepository.findById(id)
                .map(BookDto::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Book with id = " + id + " is not found"));
    }

    @Transactional(readOnly = true)
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(BookDto::toDto)
                .toList();
    }
}
