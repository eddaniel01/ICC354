// src/api/axios.ts
import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  headers: { "Content-Type": "application/json" }
});

// Interceptor para agregar el token JWT a cada request
api.interceptors.request.use(
  (config) => {
    // Aquí está el truco:
    const user = JSON.parse(localStorage.getItem("user") || "null");
    const token = user?.token;
    if (token) {
      config.headers = config.headers || {};
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default api;
