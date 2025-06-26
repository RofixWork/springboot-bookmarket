package com.book.library.services;

import com.book.library.exceptions.ApiException;
import com.book.library.exceptions.ConflictException;
import com.book.library.exceptions.NotFoundException;
import com.book.library.interfaces.IBookService;
import com.book.library.models.Book;
import com.book.library.models.Cart;
import com.book.library.models.CartItem;
import com.book.library.payloads.BookDTO;
import com.book.library.repositories.BookRepository;
import com.book.library.repositories.CartItemRepository;
import com.book.library.repositories.CartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService implements IBookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartRepository cartRepository;

    @Override
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        if(books.isEmpty()) {
            throw new ApiException("Not exist any Books", HttpStatus.OK);
        }
        return books.stream().map(book -> modelMapper.map(book, BookDTO.class)).toList();
    }

    @Override
    public BookDTO createBook(BookDTO bookDTO) {

        if (bookRepository.existsBookByTitleAndAuthor(bookDTO.getTitle(), bookDTO.getAuthor())) {
            throw new ApiException("This Book with this author already exists", HttpStatus.CONFLICT);
        } else if (bookRepository.existsBookByTitle(bookDTO.getTitle())) {
            throw new ConflictException("A Book with this title already exists");
        }

        Book book = modelMapper.map(bookDTO, Book.class);
        Book newBook = bookRepository.save(book);
        return modelMapper.map(newBook, BookDTO.class);
    }

    @Override
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book with id " + id + " not found"));

        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    public String deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book with id " + id + " not found"));
        List<CartItem> cartItems = cartItemRepository.findCartItemByBook(book);

        if(!cartItems.isEmpty())
        {
            for(CartItem cartItem : cartItems)
            {
                Cart cart = cartRepository.findById(cartItem.getCart().getId()).orElseThrow(() -> new NotFoundException("Cart with id " + cartItem.getCart().getId() + " not found"));

                Double priceToIncrease = cartItem.getPrice() * cartItem.getQuantity();
                cart.setTotalPrice(cart.getTotalPrice() - priceToIncrease);
                cartRepository.save(cart);

                cartItemRepository.delete(cartItem);

            }
        }

        bookRepository.delete(book);

        return String.format("Book with id %d has been deleted", id);
    }
}
