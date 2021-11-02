package com.mycompany.online_shop_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String addresseeName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Phone phone;

    private String email;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<OrderBook> orderBooks = new HashSet<>();

    public void addBook(Book book) {
        var orderBook = new OrderBook(this, book);
        orderBooks.add(orderBook);
    }

    public void removeBook(Book book) {
        Iterator<OrderBook> iterator = orderBooks.iterator();
        while (iterator.hasNext()) {
            var orderBook = iterator.next();
            if (orderBook.getOrder().equals(this) && orderBook.getBook().equals(book)) {
                iterator.remove();
                orderBook.setOrder(null);
                orderBook.setBook(null);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(addresseeName, order.addresseeName) &&
                Objects.equals(address, order.address) &&
                Objects.equals(phone, order.phone) &&
                Objects.equals(email, order.email) &&
                Objects.equals(createdAt, order.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addresseeName, address, phone, email, createdAt);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", addresseeName='" + addresseeName + '\'' +
                ", address=" + address +
                ", phone=" + phone +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
