import { createContext, useContext, useState, ReactNode, useEffect } from "react";
import { Book } from "../api/catalogApi";
import { addItemToCart, getCartByUsername, removeCartItem, clearUserCart } from "../api/cartApi";
import { useAuth } from "../contexts/AuthContext";

type CartItem = {
  author: ReactNode;
  id: string;
  bookId: string;
  title: string;
  price: number;
  quantity: number; // <-- Ahora obligatorio
};

interface CartContextProps {
  cart: CartItem[];
  addToCart: (book: Book) => Promise<void>;
  removeFromCart: (itemId: string) => Promise<void>;
  clearCart: () => Promise<void>;
  refreshCart: () => Promise<void>;
  increaseQuantity: (itemId: string) => void;
  decreaseQuantity: (itemId: string) => void;
}

const CartContext = createContext<CartContextProps | undefined>(undefined);

export const CartProvider = ({ children }: { children: ReactNode }) => {
  const [cart, setCart] = useState<CartItem[]>([]);
  const { user } = useAuth();

  useEffect(() => {
    if (user) {
      refreshCart();
    } else {
      setCart([]);
    }
  }, [user]);

  const refreshCart = async () => {
    if (!user) return;
    const res = await getCartByUsername(user.username);
    const data = res.data as { items: CartItem[] };
    // "Agrupar" libros iguales en frontend:
    const grouped = data.items.reduce<CartItem[]>((acc, curr) => {
      const found = acc.find(i => i.bookId === curr.bookId);
      if (found) {
        found.quantity += 1;
      } else {
        acc.push({ ...curr, quantity: 1 });
      }
      return acc;
    }, []);
    setCart(grouped);
  };

  // Suma cantidad si ya existe, si no agrega
  const addToCart = async (book: Book) => {
    if (!user) return;
    // Solo llamas al backend una vez por ítem (como antes)
    await addItemToCart(user.username, book);
    await refreshCart();
  };

  const removeFromCart = async (itemId: string) => {
    if (!user) return;
    await removeCartItem(user.username, itemId);
    await refreshCart();
  };

  const clearCart = async () => {
    if (!user) return;
    await clearUserCart(user.username);
    await refreshCart();
  };

  // LOCAL ONLY: Maneja cantidad en frontend
  const increaseQuantity = (bookId: string) => {
    setCart(prev =>
      prev.map(item =>
        item.bookId === bookId ? { ...item, quantity: item.quantity + 1 } : item
      )
    );
  };

  const decreaseQuantity = (bookId: string) => {
    setCart(prev =>
      prev
        .map(item =>
          item.bookId === bookId
            ? { ...item, quantity: Math.max(1, item.quantity - 1) }
            : item
        )
    );
  };

  return (
    <CartContext.Provider value={{ cart, addToCart, removeFromCart, clearCart, refreshCart, increaseQuantity, decreaseQuantity }}>
      {children}
    </CartContext.Provider>
  );
};

export function useCart() {
  const context = useContext(CartContext);
  if (!context) throw new Error("useCart debe usarse dentro de CartProvider");
  return context;
}
