// src/pages/Login.tsx
import { useState } from "react";
import { useForm } from "react-hook-form";
import { Button, TextField, Box, Typography, Paper, CircularProgress, Alert, Link } from "@mui/material";
import { useAuth } from "../contexts/AuthContext";
import { login as loginApi } from "../api/authApi";
import { AuthResponse } from "../types/Auth";
import { useNavigate, Navigate } from "react-router-dom";

type LoginFormInputs = { username: string; password: string };

export default function Login() {
  const { register, handleSubmit } = useForm<LoginFormInputs>();
  const { user, login } = useAuth();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  // 🚦 Si ya está autenticado, redirige por rol
  if (user) {
    if (user.roles.includes("ADMIN")) return <Navigate to="/admin" replace />;
    if (user.roles.includes("USER")) return <Navigate to="/dashboard" replace />;
    return <Navigate to="/profile" replace />;
  }

  const onSubmit = async (data: LoginFormInputs) => {
    setLoading(true);
    setError("");
    try {
      const res = await loginApi(data);
      const user: AuthResponse = res.data;
      login({
        username: user.username,
        roles: [user.role], // o user.roles si es array
        token: user.token,
      });
      // Redirecciona según rol
      if (user.role === "ADMIN") {
        navigate("/admin");
      } else if (user.role === "USER") {
        navigate("/dashboard");
      } else {
        navigate("/profile");
      }
    } catch (err: any) {
      setError("Usuario o contraseña incorrectos");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box  display="flex"
      justifyContent="center"
      alignItems="center"
      minHeight="100vh"
      sx={{
        bgcolor: "linear-gradient(135deg, #1a237e 0%, #f7f7fa 100%)"
      }}
    >
      <Paper  elevation={8}
        sx={{
          p: 5,
          width: 370,
          borderRadius: 4,
          boxShadow: "0px 4px 20px rgba(0,0,0,0.13)"
        }}
      >
        <Typography variant="h5" mb={2} align="center" color="primary">Iniciar Sesión</Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          <TextField
            label="Usuario"
            {...register("username", { required: true })}
            fullWidth margin="normal" autoFocus
          />
          <TextField
            label="Contraseña"
            type="password"
            {...register("password", { required: true })}
            fullWidth margin="normal"
          />
          {error && <Alert severity="error" sx={{ mt: 1 }}>{error}</Alert>}
          <Button
            type="submit"
            variant="contained"
            fullWidth
            sx={{ mt: 2 }}
            disabled={loading}
          >
            {loading ? <CircularProgress size={24} /> : "Ingresar"}
          </Button>
          <Typography align="center" sx={{ mt: 2 }}>
            ¿No tienes cuenta? <Link component="a" href="/register">Regístrate aquí</Link>
          </Typography>
        </form>
      </Paper>
    </Box>
  );
}
