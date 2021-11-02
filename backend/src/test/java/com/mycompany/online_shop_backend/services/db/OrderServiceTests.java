package com.mycompany.online_shop_backend.services.db;

import com.mycompany.online_shop_backend.domain.*;
import com.mycompany.online_shop_backend.dto.services.BookDto;
import com.mycompany.online_shop_backend.dto.request.OrderRequest;
import com.mycompany.online_shop_backend.exceptions.EntityNotFoundException;
import com.mycompany.online_shop_backend.repositories.BookRepository;
import com.mycompany.online_shop_backend.repositories.OrderBookRepository;
import com.mycompany.online_shop_backend.repositories.OrderRepository;
import com.mycompany.online_shop_backend.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Import(OrderService.class)
@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

    private final Author author = new Author(1L, "Author One");
    private final Book book = new Book(
            1L,
            "Book One",
            "Description One",
            author,
            "/imageOne",
            22.95
    );
    private final Order order = new Order(
            1L,
            "Name One Surname One",
            new Address(1L, "Address, 1"),
            new Phone(1L, "+1111 1111"),
            "userOne@test.com",
            LocalDateTime.now(),
            new HashSet<>()
    );
    private final OrderBook orderBook = new OrderBook(order, book);
    private final OrderRequest orderRequest = new OrderRequest(
            order.getAddresseeName(),
            order.getAddress().getValue(),
            order.getPhone().getValue(),
            order.getEmail(),
            List.of(BookDto.toDto(book))
    );

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderBookRepository orderBookRepository;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private OrderService orderService;

    @BeforeEach
    void setup() {
        order.setOrderBooks(Set.of(orderBook));
    }

    @Test
    public void saveOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        doNothing().when(orderBookRepository).saveOrderBook(any(OrderBook.class));

        orderService.save(orderRequest);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderBookRepository, times(1)).saveOrderBook(any(OrderBook.class));
    }

    @Test
    public void saveOrderWithNonExistingBook() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(bookRepository.findById(book.getId())).thenThrow(new EntityNotFoundException("not found"));

        assertThrows(EntityNotFoundException.class, () -> orderService.save(orderRequest));
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderBookRepository, never()).saveOrderBook(any(OrderBook.class));
    }
}
