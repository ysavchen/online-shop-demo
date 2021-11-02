package com.mycompany.online_shop_backend.repositories;

import com.mycompany.online_shop_backend.AbstractRepositoryTest;
import com.mycompany.online_shop_backend.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderBookRepositoryTests extends AbstractRepositoryTest {

    private final Author author = new Author(1, "Author One");
    private final Book book = new Book(
            1L,
            "Book One",
            "Description One",
            author,
            "/imageOne",
            22.95
    );
    private final Order order = new Order(
            100L,
            "Test Addressee",
            new Address(1L, "Address, 1"),
            new Phone(1L, "+1111 1111"),
            "testOrderBook@test.com",
            LocalDateTime.now(),
            new HashSet<>()
    );
    private final OrderBook orderBook = new OrderBook(order, book);

    @Autowired
    private TestEntityManager em;

    @Autowired
    private OrderBookRepository orderBookRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setup() {
        order.setOrderBooks(Set.of(orderBook));
    }

    @Test
    public void saveOrderBook() {
        Order savedOrder = orderRepository.save(order);
        orderBookRepository.saveOrderBook(orderBook);

        assertThat(em.find(OrderBook.class, orderBook.getOrderBookId()))
                .hasFieldOrPropertyWithValue("order", savedOrder)
                .hasFieldOrPropertyWithValue("book", book);
    }
}
