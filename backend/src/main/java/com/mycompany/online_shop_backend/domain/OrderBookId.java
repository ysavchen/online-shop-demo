package com.mycompany.online_shop_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class OrderBookId implements Serializable {

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "book_id")
    private long bookId;

}
