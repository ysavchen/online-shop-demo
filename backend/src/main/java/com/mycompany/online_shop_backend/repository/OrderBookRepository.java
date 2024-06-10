package com.mycompany.online_shop_backend.repository;

import com.mycompany.online_shop_backend.repository.domain.OrderBook;
import com.mycompany.online_shop_backend.repository.domain.OrderBookId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OrderBookRepository extends JpaRepository<OrderBook, OrderBookId> {

    @Modifying
    @Query(value = "insert into order_book (order_id, book_id) " +
            "values (:#{#orderBook.order.id}, :#{#orderBook.book.id})", nativeQuery = true)
    void saveOrderBook(OrderBook orderBook);
}
