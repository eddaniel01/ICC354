import { useAuth } from "../contexts/AuthContext";
import Cart from "./Cart";
import Catalog from "./Catalog";

export default function UserDashboard() {
  useAuth();
  return (
    <div>
      <Catalog />
      <Cart />
    </div>
  );
}
