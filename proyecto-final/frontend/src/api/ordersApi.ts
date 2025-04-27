// src/api/ordersApi.ts
import api from "./axios";

export type Order = {
    id: number;
    totalAmount: number;
    date: string;
  };

export const createOrder = async (username: string) => {
  return api.post(`/api/orders/${username}`);
};

// Obtener historial de compras del usuario
export const getOrdersByUsername = async (username: string) => {
  return api.get<Order[]>(`/api/orders/${username}`);
};