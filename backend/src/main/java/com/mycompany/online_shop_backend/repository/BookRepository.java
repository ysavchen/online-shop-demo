package com.mycompany.online_shop_backend.repository;

import com.mycompany.online_shop_backend.repository.domain.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    //todo: не очень переопределять дефолтные методы
    @Override
    @EntityGraph("book-entity-graph")
    List<Book> findAll();
}
