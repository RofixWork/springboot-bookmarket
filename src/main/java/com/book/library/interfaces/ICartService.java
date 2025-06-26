package com.book.library.interfaces;

import com.book.library.payloads.BookDTO;
import com.book.library.payloads.CartDTO;
import com.book.library.payloads.CartResponse;
import jakarta.transaction.Transactional;

public interface ICartService {

    CartDTO createNewCart();

    CartResponse addBookToCart(Long cartId, Long bookId, Integer quantity);

    @Transactional
    String deleteBookInCart(Long cartId, Long bookId);

    CartResponse getCartItems(Long cartId);

    @Transactional
    CartResponse updateQuantity(Long cartId, Long bookId, Integer newQuantity);

    String deleteCart(Long cartId);

    @Transactional
    String checkout(Long cartId);
}
