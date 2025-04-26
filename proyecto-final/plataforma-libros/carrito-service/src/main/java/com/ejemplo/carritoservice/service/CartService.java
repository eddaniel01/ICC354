package com.ejemplo.carritoservice.service;

import com.ejemplo.carritoservice.model.Cart;
import com.ejemplo.carritoservice.model.CartItem;
import com.ejemplo.carritoservice.repository.CartItemRepository;
import com.ejemplo.carritoservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Cart getCartByUsername(String username) {
        List<Cart> carts = cartRepository.findAllByUsername(username);
        if (carts.size() > 1) {
            throw new IllegalStateException("Se encontraron múltiples carritos para el usuario: " + username);
        }
        // Si no hay, crea uno nuevo
        return carts.isEmpty()
                ? cartRepository.save(Cart.builder().username(username).build())
                : carts.get(0);
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
