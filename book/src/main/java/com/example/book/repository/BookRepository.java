package com.example.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.book.entity.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn); // where isbn = 'A1010100'

    List<Book> findByTitleContaining(String title); // where title like '%파워%'

    // where author = ''
    List<Book> findByAuthor(String author);

    // where author like '%영' findByFirstnameEndingWith
    List<Book> findByAuthorEndingWith(String author);

    // where author like '박%' findByFirstnameStartingWith
    List<Book> findByAuthorStartingWith(String author);

    // where author like '%진수%' findByFirstnameContaining
    List<Book> findByAuthorContaining(String author);

    // 도서가격이 12000 이상 35000 이하
    List<Book> findByPriceBetween(int startPrice, int endPrice);
}