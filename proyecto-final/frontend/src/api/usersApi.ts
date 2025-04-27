import api from "./axios";

export type User = {
  id: string;
  username: string;
  role: string;
  password?: string;
};

export const getAllUsers = () => api.get<User[]>("/api/auth/users");
export const createUser = (user: Partial<User> & { password: string }) => api.post("/api/auth/users", user);
export const updateUser = (id: string, user: Partial<User>) => api.put(`/api/auth/users/${id}`, user);
export const deleteUser = (id: string) => api.delete(`/api/auth/users/${id}`);
export const getUserById = (id: string) => api.get<User>(`/api/auth/users/${id}`);
export const updateMyProfile = (user: Partial<User>) => api.put("/api/auth/users/me", user);
export const getMyProfile = () => api.get<User>("/api/auth/users/me");


