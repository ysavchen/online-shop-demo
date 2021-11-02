package com.mycompany.online_shop_backend.repositories;

import com.mycompany.online_shop_backend.domain.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph("book-entity-graph")
    List<Book> findAll();
}
