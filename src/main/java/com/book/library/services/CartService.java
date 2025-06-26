package com.book.library.services;

import com.book.library.exceptions.BadRequestException;
import com.book.library.exceptions.NotFoundException;
import com.book.library.interfaces.ICartService;
import com.book.library.models.Book;
import com.book.library.models.Cart;
import com.book.library.models.CartItem;
import com.book.library.payloads.BookDTO;
import com.book.library.payloads.CartDTO;
import com.book.library.payloads.CartResponse;
import com.book.library.repositories.BookRepository;
import com.book.library.repositories.CartItemRepository;
import com.book.library.repositories.CartRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService implements ICartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartDTO createNewCart() {
        Cart cart = cartRepository.save(new Cart());
        return modelMapper.map(cart, CartDTO.class);
    }

    @Override
    public CartResponse addBookToCart(Long cartId, Long bookId, Integer quantity) {
        Cart currentCart = getCart(cartId);
        Book book = getBook(bookId);

        CartItem isAlreadyExistInTheCart = cartItemRepository.findCartItemByCartAndBook(currentCart, book);

        if(isAlreadyExistInTheCart != null)
        {
            throw new BadRequestException("Book already exist in the cart");
        }

        if (book.getQuantity() == 0) {
            throw new BadRequestException("This book is currently out of stock.");
        }

        if (quantity <= 0) {
            throw new BadRequestException("Requested quantity must be greater than 0.");
        }

        if (quantity > book.getQuantity()) {
            throw new BadRequestException("Requested quantity exceeds available stock (" + book.getQuantity() + ").");
        }

        CartItem cartItem = new CartItem();
        cartItem.setTitle(book.getTitle());
        cartItem.setAuthor(book.getAuthor());
        cartItem.setCart(currentCart);
        cartItem.setBook(book);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(book.getPrice());
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        Double priceToAdd = savedCartItem.getPrice() * quantity;
        currentCart.setTotalPrice(currentCart.getTotalPrice() + priceToAdd);
        cartRepository.save(currentCart);

        return getCartResponse(currentCart);
    }

    private CartResponse getCartResponse(Cart currentCart) {
        CartResponse cartResponse = modelMapper.map(currentCart, CartResponse.class);
        List<BookDTO> bookDTOS = currentCart.getCartItems().stream()
                .map(item -> {
                    BookDTO bookDTO = modelMapper.map(item, BookDTO.class);
                    bookDTO.setQuantity(item.getQuantity());
                    return bookDTO;
                }).toList();
        cartResponse.setCartItems(bookDTOS);
        return cartResponse;
    }

    @Transactional
    @Override
    public String deleteBookInCart(Long cartId, Long bookId) {
        Cart currentCart = getCart(cartId);
        Book book = getBook(bookId);
        CartItem cartItem = cartItemRepository.findCartItemByCartAndBook(currentCart, book);
        if(cartItem == null)
        {
            throw new BadRequestException("Book not found in the Cart!!!");
        }
        //update total price
        Double priceToIncrease = cartItem.getPrice() * cartItem.getQuantity();
        currentCart.setTotalPrice(currentCart.getTotalPrice() - priceToIncrease);
        cartRepository.save(currentCart);

        cartItemRepository.delete(cartItem);

        return String.format("Book '%s' has been deleted successfully from Cart.", book.getTitle());
    }

    private Book getBook(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
    }

    @Override
    public CartResponse getCartItems(Long cartId) {
        Cart currentCart = getCart(cartId);

        return getCartResponse(currentCart);
    }

    @Transactional
    @Override
    public CartResponse updateQuantity(Long cartId, Long bookId, Integer newQuantity) {
        Cart currentCart = getCart(cartId);
        Book book = getBook(bookId);
        CartItem cartItem = cartItemRepository.findCartItemByCartAndBook(currentCart, book);

        if(cartItem == null)
            throw new BadRequestException("Book not found in the Cart!!!");

        if (newQuantity <= 0)
        {
            throw new BadRequestException("Requested quantity must be greater than 0.");
        }
        if (newQuantity > book.getQuantity())
        {
            throw new BadRequestException("Requested quantity exceeds available stock (" + book.getQuantity() + ").");
        }

        //before calc new total price
        Double priceToIncrease = cartItem.getPrice() * cartItem.getQuantity();
        currentCart.setTotalPrice(currentCart.getTotalPrice() - priceToIncrease);
        cartRepository.save(currentCart);

        cartItem.setPrice(book.getPrice());
        cartItem.setQuantity(newQuantity);
        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        //update total price
        Double newPriceToAdd = updatedCartItem.getPrice() * newQuantity;
        currentCart.setTotalPrice(currentCart.getTotalPrice() + newPriceToAdd);
        cartRepository.save(currentCart);

        return getCartResponse(currentCart);
    }

    @Override
    public String deleteCart(Long cartId) {
        Cart currentCart = getCart(cartId);
        cartRepository.delete(currentCart);
        return "Cart with id " + currentCart.getId() + " has been deleted successfully from Cart.";
    }

    @Transactional
    @Override
    public String checkout(Long cartId) {
        Cart currentCart = getCart(cartId);
        List<CartItem> cartItems = cartItemRepository.findCartItemByCart(currentCart);

        if(cartItems.isEmpty())
            throw new BadRequestException("Cart is empty");

        for (CartItem cartItem : cartItems) {
            Book book = cartItem.getBook();

            book.setQuantity(book.getQuantity() - cartItem.getQuantity());
            bookRepository.save(book);

            cartItemRepository.delete(cartItem);
        }

        Double totalPrice = currentCart.getTotalPrice();
        currentCart.setTotalPrice(0.00);
        cartRepository.save(currentCart);

        return "Your purchase of " + cartItems.size() + " books totals $" +  String.format("%.2f", totalPrice)  + ".";
    }

    private Cart getCart(Long cartId)
    {
        return cartRepository.findById(cartId).orElseThrow(
                () -> new NotFoundException("Not exist any cart with Id: " + cartId)
        );
    }
}
