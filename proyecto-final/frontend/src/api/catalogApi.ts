import api from "./axios";

export interface Book {
  id: string;
  title: string;
  author: string;
  genre: string;
  price: number;
}

export const getAllBooks = async (): Promise<Book[]> => {
  const res = await api.get<Book[]>("/api/catalog"); 
  return res.data;
};

export const searchBooks = async (query: string): Promise<Book[]> => {
  const res = await api.get<Book[]>("/api/catalog/search", { params: { q: query } });
  return res.data;
};
