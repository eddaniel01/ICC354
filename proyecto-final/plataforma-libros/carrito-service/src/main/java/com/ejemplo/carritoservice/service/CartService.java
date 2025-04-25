package com.ejemplo.carritoservice.service;

import com.ejemplo.carritoservice.model.Cart;
import com.ejemplo.carritoservice.model.CartItem;
import com.ejemplo.carritoservice.model.Order;
import com.ejemplo.carritoservice.repository.CartItemRepository;
import com.ejemplo.carritoservice.repository.CartRepository;
import com.ejemplo.carritoservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public Cart getCartByUser(String username) {
        return cartRepository.findByUsername(username).orElseGet(() -> {
            Cart newCart = Cart.builder().username(username).build();
            return cartRepository.save(newCart);
        });
    }

    public Cart addItemToCart(String username, CartItem item) {
        Cart cart = getCartByUser(username);
        item.setCart(cart);
        cart.getItems().add(cartItemRepository.save(item));
        return cartRepository.save(cart);
    }

    public void removeItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    public Order confirmOrder(String username) {
        Cart cart = getCartByUser(username);
        double total = cart.getItems().stream().mapToDouble(CartItem::getPrice).sum();

        Order order = Order.builder()
                .username(username)
                .totalAmount(total)
                .build();

        cart.getItems().clear(); // vaciar carrito
        cartRepository.save(cart);
        return orderRepository.save(order);
    }

    public List<Order> getOrders(String username) {
        return orderRepository.findByUsername(username);
    }
}
