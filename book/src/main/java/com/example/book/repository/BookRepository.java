package com.example.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.example.book.entity.Book;
import com.example.book.entity.QBook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, QuerydslPredicateExecutor<Book> {

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

    public default Predicate makePredicate(String type, String keyword) {
        BooleanBuilder builder = new BooleanBuilder();
        QBook book = QBook.book;

        builder.and(book.id.gt(0)); // where b.id > 0

        if (type == null) {
            return builder;
        }

        // type == 't'(title) / type == 'a'(author)
        if (type.equals("t")) {
            // title like '%파워%'
            builder.and(book.title.contains(keyword));
        } else {
            // author like '%천인%'
            builder.and(book.author.contains(keyword));
        }

        return builder;
    }
}