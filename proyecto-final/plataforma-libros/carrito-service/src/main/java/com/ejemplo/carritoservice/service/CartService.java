package com.ejemplo.carritoservice.service;

import com.ejemplo.carritoservice.model.Cart;
import com.ejemplo.carritoservice.model.CartItem;
import com.ejemplo.carritoservice.repository.CartItemRepository;
import com.ejemplo.carritoservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Cart getCartByUsername(String username) {
        return cartRepository.findByUsername(username).orElseGet(() -> {
            Cart newCart = Cart.builder().username(username).build();
            return cartRepository.save(newCart);
        });
    }

    public Cart addItem(String username, String bookId, String title, double price) {
        Cart cart = getCartByUsername(username);

        CartItem item = CartItem.builder()
                .bookId(bookId)
                .title(title)
                .price(price)
                .cart(cart)
                .build();

        cart.getItems().add(item);
        cartRepository.save(cart);
        return cart;
    }

    public Cart removeItem(String username, Long itemId) {
        Cart cart = getCartByUsername(username);
        Optional<CartItem> itemToRemove = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst();

        itemToRemove.ifPresent(item -> {
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
        });

        return cartRepository.save(cart);
    }

    public Cart clearCart(String username) {
        Cart cart = getCartByUsername(username);
        cart.getItems().clear();
        cartItemRepository.deleteAll();
        return cartRepository.save(cart);
    }
}
