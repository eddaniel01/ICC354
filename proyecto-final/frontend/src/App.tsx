import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import ProtectedRoute from "./components/ProtectedRoute";
import AdminDashboard from "./pages/AdminDashboard";
import Profile from "./pages/Profile";
import UserDashboard from "./pages/UserDashboard";
import Navbar from "./components/Navbar";
import Catalog from "./pages/Catalog";
import Cart from "./pages/Cart";
import { CartProvider } from "./contexts/CartContext";
// Aquí puedes importar más páginas cuando las tengas

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Rutas públicas: NO Navbar, NO CartProvider */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Rutas protegidas: SÍ Navbar, SÍ CartProvider */}
        <Route
          path="*"
          element={
            <CartProvider>
              <Navbar />
              <Routes>
                <Route
                  path="/profile"
                  element={
                    <ProtectedRoute>
                      <Profile />
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="/admin"
                  element={
                    <ProtectedRoute allowedRoles={["ADMIN"]}>
                      <AdminDashboard />
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="/dashboard"
                  element={
                    <ProtectedRoute allowedRoles={["USER"]}>
                      <UserDashboard />
                    </ProtectedRoute>
                  }
                />
                <Route path="/catalog" element={<Catalog />} />
                <Route path="/cart" element={<Cart />} />
                {/* Puedes agregar un Home aquí si lo deseas */}
                {/* <Route path="/" element={<Home />} /> */}
                {/* Ruta por defecto: Redirige a catálogo si está logueado */}
                <Route path="*" element={<Navigate to="/catalog" replace />} />
              </Routes>
            </CartProvider>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
