// src/api/cartApi.ts
import api from "./axios";
import { Book } from "./catalogApi";

// Agrega libro al carrito del usuario
export const addItemToCart = async (username: string, book: Book) => {
  return api.post(`/api/cart/${username}/add`, null, {
    params: {
      bookId: book.id,
      title: book.title,
      price: book.price
    }
  });
};

// Obtener el carrito de un usuario
export const getCartByUsername = async (username: string) => {
  return api.get(`/api/cart/${username}`);
};

// Eliminar un ítem
export const removeCartItem = async (username: string, itemId: string) => {
  return api.delete(`/api/cart/${username}/remove/${itemId}`);
};

// Vaciar carrito
export const clearUserCart = async (username: string) => {
  return api.delete(`/api/cart/${username}/clear`);
};
