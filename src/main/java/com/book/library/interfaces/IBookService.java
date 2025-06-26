package com.book.library.interfaces;

import com.book.library.payloads.BookDTO;

import java.util.List;

public interface IBookService {
    List<BookDTO> getAllBooks();
    BookDTO createBook(BookDTO bookDTO);
    BookDTO getBookById(Long id);

    String deleteBook(Long bookId);
}
