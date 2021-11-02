package com.mycompany.online_shop_backend.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "order_book")
public class OrderBook {

    @EmbeddedId
    private OrderBookId orderBookId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("book_id")
    private Book book;

    //todo: quantity for same books

    private OrderBook() {
    }

    public OrderBook(Order order, Book book) {
        this.order = order;
        this.book = book;
        this.orderBookId = new OrderBookId(order.getId(), book.getId());
    }
}
