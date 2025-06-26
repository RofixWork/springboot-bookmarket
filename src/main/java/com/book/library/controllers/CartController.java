package com.book.library.controllers;

import com.book.library.interfaces.ICartService;
import com.book.library.models.Cart;
import com.book.library.payloads.ApiResponse;
import com.book.library.payloads.BookDTO;
import com.book.library.payloads.CartDTO;
import com.book.library.payloads.CartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CartController {
    @Autowired
    ICartService cartService;

    @GetMapping("/carts/{cartId}")
    public ResponseEntity<CartResponse> getCartItems(@PathVariable Long cartId) {
        CartResponse cartResponse = cartService.getCartItems(cartId);
        return new ResponseEntity<>(cartResponse, HttpStatus.OK);
    }

    @PostMapping("/carts")
    public ResponseEntity<CartDTO> addCart(@RequestBody Cart cart){
        CartDTO cartDTO = cartService.createNewCart();

        return ResponseEntity.status(HttpStatus.CREATED).body(cartDTO);
    }

    @PostMapping("/carts/{cartId}/add")
    public ResponseEntity<CartResponse> addBookToCart(
            @PathVariable Long cartId,
            @RequestParam(name = "bookId") Long bookId,
            @RequestParam(name = "quantity") Integer quantity
    )
    {
        CartResponse cartResponse = cartService.addBookToCart(cartId, bookId, quantity);

        return ResponseEntity.ok(cartResponse);
    }

    @DeleteMapping("/carts/{cartId}/remove/{bookId}")
    public ResponseEntity<ApiResponse> deleteProductInCart(@PathVariable Long cartId, @PathVariable Long bookId){
        String status = cartService.deleteBookInCart(cartId, bookId);

        return ResponseEntity.ok(new ApiResponse(status, true));
    }

    @PutMapping("/carts/{cartId}/update-quantity")
    public ResponseEntity<CartResponse> updateQuantity(
            @PathVariable Long cartId,
            @RequestParam(name = "bookId") Long bookId,
            @RequestParam(name = "newQuantity") Integer newQuantity
    ){
        CartResponse cartResponse = cartService.updateQuantity(cartId, bookId, newQuantity);

        return ResponseEntity.ok(cartResponse);
    }

    @DeleteMapping("/carts/{cartId}")
    public ResponseEntity<ApiResponse> deleteCart(@PathVariable Long cartId){
        String status = cartService.deleteCart(cartId);
        return ResponseEntity.ok(new ApiResponse(status, true));
    }

    @PostMapping("/carts/{cartId}/checkout")
    public ResponseEntity<ApiResponse> checkout(
            @PathVariable Long cartId
    )
    {
        String cartResponse = cartService.checkout(cartId);
        return ResponseEntity.ok(new ApiResponse(cartResponse, true));
    }
}
