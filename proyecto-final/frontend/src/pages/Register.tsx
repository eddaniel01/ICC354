import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { Button, TextField, Box, Typography, Paper, CircularProgress, Alert } from "@mui/material";
import { register as registerApi } from "../api/authApi";
import { useNavigate } from "react-router-dom";

type RegisterFormInputs = {
  username: string;
  password: string;
  confirmPassword: string;
};

export default function Register() {
  const { register, handleSubmit, watch } = useForm<RegisterFormInputs>();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const navigate = useNavigate();

  const onSubmit = async (data: RegisterFormInputs) => {
    setError(""); setSuccess(""); setLoading(true);
    if (data.password !== data.confirmPassword) {
      setError("Las contraseñas no coinciden");
      setLoading(false);
      return;
    }
    try {
      await registerApi({ username: data.username, password: data.password });
      setSuccess("¡Registro exitoso! Redireccionando al login...");
      setTimeout(() => navigate("/login"), 1500); // Redirige al login después de 1.5s
    } catch (err: any) {
      setError("Error al registrar. ¿Usuario ya existe?");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh" bgcolor="#222">
      <Paper elevation={5} sx={{ p: 4, width: 360 }}>
        <Typography variant="h5" mb={2} align="center" color="primary">Registrarse</Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          <TextField
            label="Usuario"
            {...register("username", { required: true })}
            fullWidth margin="normal" autoFocus
          />
          <TextField
            label="Contraseña"
            type="password"
            {...register("password", { required: true, minLength: 4 })}
            fullWidth margin="normal"
          />
          <TextField
            label="Confirmar Contraseña"
            type="password"
            {...register("confirmPassword", { required: true })}
            fullWidth margin="normal"
          />
          {error && <Alert severity="error" sx={{ mt: 1 }}>{error}</Alert>}
          {success && <Alert severity="success" sx={{ mt: 1 }}>{success}</Alert>}
          <Button
            type="submit"
            variant="contained"
            fullWidth
            sx={{ mt: 2 }}
            disabled={loading}
          >
            {loading ? <CircularProgress size={24} /> : "Registrarse"}
          </Button>
        </form>
      </Paper>
    </Box>
  );
}
