export interface Book {
    id: string;
    title: string;
    author: string;
    genre: string;
    price: number;
  }
  
  // Si usas fetch:
  export const getAllBooks = async (): Promise<Book[]> => {
    const res = await fetch("http://localhost:8081/api/catalog");
    if (!res.ok) throw new Error("Error cargando libros");
    return res.json();
  };
