import api from "./axios";
import { AuthResponse } from "../types/Auth";

export const login = (data: { username: string; password: string }) =>
  api.post<AuthResponse>("/api/auth/login", data);

export const register = (data: any) =>
  api.post("/api/auth/register", data);
