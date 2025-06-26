package com.book.library.controllers;

import com.book.library.interfaces.IBookService;
import com.book.library.payloads.ApiResponse;
import com.book.library.payloads.BookDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BookController {

    @Autowired
    IBookService bookService;

    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> booksDTOS = bookService.getAllBooks();

        return ResponseEntity.ok(booksDTOS);
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<BookDTO> getBookBy(@PathVariable Long bookId) {
        BookDTO booksDTOS = bookService.getBookById(bookId);

        return ResponseEntity.ok(booksDTOS);
    }

    @PostMapping("/books")
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO)
    {
        BookDTO book = bookService.createBook(bookDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable Long bookId) {
        String status = bookService.deleteBook(bookId);
        return ResponseEntity.ok(new ApiResponse(status, true));
    }
}
