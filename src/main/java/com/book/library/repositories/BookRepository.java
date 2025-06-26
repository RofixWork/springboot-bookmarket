package com.book.library.repositories;

import com.book.library.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsBookByTitle(String title);
    boolean existsBookByTitleAndAuthor(String title, String author);
}
