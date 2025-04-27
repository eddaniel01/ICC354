// src/api/ordersApi.ts
import api from "./axios";

export const createOrder = async (username: string) => {
  return api.post(`/api/orders/${username}`);
};
