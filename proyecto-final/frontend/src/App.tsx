// src/App.tsx
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
// Aquí puedes importar más páginas cuando las tengas

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Página de Login */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        {/* Cuando agregues un Home (ejemplo) */}
        {/* <Route path="/" element={<Home />} /> */}

        {/* Ruta por defecto: cualquier otra ruta redirige a /login */}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
