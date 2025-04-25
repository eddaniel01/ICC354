// src/pages/Login.tsx
import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { Button, TextField, Box, Typography, Paper, CircularProgress, Alert, Link } from "@mui/material";
import { useAuth } from "../contexts/AuthContext";
import { login as loginApi } from "../api/authApi";
import { AuthResponse } from "../types/Auth";


type LoginFormInputs = { username: string; password: string };

export default function Login() {
  const { register, handleSubmit } = useForm<LoginFormInputs>();
  const { login } = useAuth();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const onSubmit = async (data: LoginFormInputs) => {
    setLoading(true);
    setError("");
    try {
      const res = await loginApi(data);
      // Forzar el tipo (ya está tipado si hiciste el cambio en authApi.ts)
      const user: AuthResponse = res.data;
      login({
        username: user.username,
        roles: [user.role], // O user.roles si en backend es array
        token: user.token,
      });
      window.location.href = "/";
    } catch (err: any) {
      setError("Usuario o contraseña incorrectos");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh" bgcolor="#222">
      <Paper elevation={5} sx={{ p: 4, width: 360 }}>
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
