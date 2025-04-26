import { AppBar, Toolbar, Button, Typography, Avatar, Box } from "@mui/material";
import { Link, useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const isAuthPage = location.pathname === "/login" || location.pathname === "/register";

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <AppBar position="sticky" color="primary" elevation={2}>
      <Toolbar>
        <Typography variant="h5" sx={{ flexGrow: 1, fontWeight: 700, letterSpacing: 1 }}>
          Book Store
        </Typography>
        {/* Mostrar solo los botones de login/register si está en esas páginas */}
        {isAuthPage ? (
          <>
            <Button color="inherit" component={Link} to="/login" sx={{ mx: 1 }}>
              Iniciar Sesión
            </Button>
            <Button color="inherit" component={Link} to="/register" sx={{ mx: 1 }}>
              Registrarse
            </Button>
          </>
        ) : user ? (
          <>
            <Box sx={{ display: "flex", alignItems: "center", mr: 2 }}>
              <Avatar sx={{ width: 32, height: 32, bgcolor: "secondary.main", mr: 1 }}>
                {user.username.charAt(0).toUpperCase()}
              </Avatar>
              <Typography variant="body1" sx={{ fontWeight: 500, mr: 2 }}>
                ¡Hola, {user.username}!
              </Typography>
            </Box>
            <Button color="inherit" component={Link} to="/profile" sx={{ mx: 1 }}>
              Perfil
            </Button>
            {user.roles.includes("ADMIN") && (
              <Button color="inherit" component={Link} to="/admin" sx={{ mx: 1 }}>
                Admin
              </Button>
            )}
            {user.roles.includes("USER") && (
              <Button color="inherit" component={Link} to="/dashboard" sx={{ mx: 1 }}>
                Dashboard
              </Button>
            )}
            <Button color="inherit" onClick={handleLogout} sx={{ mx: 1 }}>
              Cerrar sesión
            </Button>
          </>
        ) : (
          <>
            <Button color="inherit" component={Link} to="/login" sx={{ mx: 1 }}>
              Iniciar Sesión
            </Button>
            <Button color="inherit" component={Link} to="/register" sx={{ mx: 1 }}>
              Registrarse
            </Button>
          </>
        )}
      </Toolbar>
    </AppBar>
  );
}
