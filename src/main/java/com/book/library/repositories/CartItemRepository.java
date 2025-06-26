package com.book.library.repositories;

import com.book.library.models.Book;
import com.book.library.models.Cart;
import com.book.library.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findCartItemByCartAndBook(Cart cart, Book book);
    
    List<CartItem> findCartItemByBook(Book book);

    List<CartItem> findCartItemByCart(Cart cart);
}
