import { useState } from "react";
import {
  Box, Paper, Typography, Avatar, Button, TextField, Divider, Stack, Alert, Dialog, DialogTitle, DialogContent, DialogActions, CircularProgress, Fade
} from "@mui/material";
import LockIcon from "@mui/icons-material/Lock";
import PersonIcon from "@mui/icons-material/Person";
import AdminPanelSettingsIcon from "@mui/icons-material/AdminPanelSettings";
import { useAuth } from "../contexts/AuthContext";
import { updateMyProfile } from "../api/usersApi";
import { useNavigate } from "react-router-dom";

export default function Profile() {
  const { user, login, logout  } = useAuth();
  const [editOpen, setEditOpen] = useState(false);
  const [passwordOpen, setPasswordOpen] = useState(false);
  const [username, setUsername] = useState(user?.username || "");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate()

  // Editar usuario autenticado (solo username)
  const handleEditSave = async () => {
    setLoading(true);
    setError("");
    setSuccess("");
    try {
      await updateMyProfile({ username });
      // 🔥 Forzar logout y redirección tras cambio de username
      logout(); // Limpia el user del contexto y localStorage
      setSuccess("Usuario actualizado. Debes iniciar sesión de nuevo.");
      setTimeout(() => {
        navigate("/login");
      }, 6000); // 1.5 segundos de feedback antes de redirigir
    } catch (err) {
      setError("No se pudo actualizar el usuario.");
    } finally {
      setLoading(false);
    }
  };

  // Cambiar contraseña del autenticado
  const handlePasswordSave = async () => {
    setError(""); setSuccess("");
    if (newPassword !== confirmPassword) {
      setError("Las contraseñas no coinciden.");
      return;
    }
    setLoading(true);
    try {
      await updateMyProfile({ password: newPassword });
      setSuccess("Contraseña actualizada.");
      setPasswordOpen(false);
      setNewPassword("");
      setConfirmPassword("");
    } catch (err) {
      setError("No se pudo cambiar la contraseña.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ maxWidth: 500, mx: "auto", mt: { xs: 4, md: 8 } }}>
      <Paper elevation={6} sx={{
        p: 4,
        borderRadius: 4,
        boxShadow: "0 10px 42px 2px rgba(30,60,110,0.10)",
        bgcolor: "#fff"
      }}>
        <Stack spacing={2} alignItems="center">
          <Avatar sx={{
            width: 80, height: 80, bgcolor: "#1976d2",
            boxShadow: "0 2px 18px 1px rgba(30,60,110,0.15)"
          }}>
            <PersonIcon fontSize="large" />
          </Avatar>
          <Typography variant="h4" fontWeight={700} gutterBottom>
            Perfil de Usuario
          </Typography>
          <Divider sx={{ width: "100%", my: 1.5 }} />

          {/* Datos */}
          <Stack spacing={1.5} sx={{ width: "100%" }}>
            <Stack direction="row" alignItems="center" spacing={1}>
              <AdminPanelSettingsIcon color={user?.roles?.includes("ADMIN") ? "success" : "primary"} />
              <Typography color="text.secondary">ID:</Typography>
              <Typography fontWeight={600}>{typeof user?.id === "string" || typeof user?.id === "number" ? String(user.id) : "N/A"}</Typography>
            </Stack>
            <Stack direction="row" alignItems="center" spacing={1}>
              <PersonIcon color="primary" />
              <Typography color="text.secondary">Usuario:</Typography>
              <Typography fontWeight={600}>{user?.username}</Typography>
            </Stack>
            <Stack direction="row" alignItems="center" spacing={1}>
              <AdminPanelSettingsIcon color={user?.roles?.includes("ADMIN") ? "success" : "primary"} />
              <Typography color="text.secondary">Rol:</Typography>
              <Typography
                fontWeight={600}
                sx={{
                  px: 1.7, py: 0.5,
                  borderRadius: 2,
                  bgcolor: user?.roles?.includes("ADMIN") ? "#e3fcef" : "#e3e7ed",
                  color: user?.roles?.includes("ADMIN") ? "#219653" : "#374151"
                }}>
                {user?.roles?.join(", ")}
              </Typography>
            </Stack>
          </Stack>

          <Divider sx={{ width: "100%", my: 2 }} />
          <Stack direction={{ xs: "column", sm: "row" }} spacing={2} sx={{ width: "100%" }}>
            <Button
              variant="outlined"
              fullWidth
              onClick={() => { setEditOpen(true); setUsername(user?.username || ""); }}
              sx={{ borderRadius: 3 }}
            >
              Editar Usuario
            </Button>
            <Button
              variant="contained"
              startIcon={<LockIcon />}
              fullWidth
              onClick={() => setPasswordOpen(true)}
              sx={{ borderRadius: 3 }}
            >
              Cambiar Contraseña
            </Button>
          </Stack>

          {/* Alertas */}
          <Fade in={!!error}>
            <div style={{ width: "100%" }}>
              {error && <Alert severity="error">{error}</Alert>}
            </div>
          </Fade>
          <Fade in={!!success}>
            <div style={{ width: "100%" }}>
              {success && <Alert severity="success">{success}</Alert>}
            </div>
          </Fade>
        </Stack>
      </Paper>

      {/* Modal editar usuario */}
      <Dialog open={editOpen} onClose={() => setEditOpen(false)} PaperProps={{ sx: { borderRadius: 3 } }}>
        <DialogTitle>Editar Usuario</DialogTitle>
        <DialogContent>
          <TextField
            label="Usuario"
            fullWidth
            value={username}
            onChange={e => setUsername(e.target.value)}
            sx={{ mt: 1 }}
            autoFocus
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setEditOpen(false)}>Cancelar</Button>
          <Button variant="contained" onClick={handleEditSave} disabled={loading}>
            {loading ? <CircularProgress size={20} /> : "Guardar"}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Modal cambiar contraseña */}
      <Dialog open={passwordOpen} onClose={() => setPasswordOpen(false)} PaperProps={{ sx: { borderRadius: 3 } }}>
        <DialogTitle>Cambiar Contraseña</DialogTitle>
        <DialogContent>
          <TextField
            label="Nueva Contraseña"
            type="password"
            fullWidth
            value={newPassword}
            onChange={e => setNewPassword(e.target.value)}
            sx={{ mt: 1 }}
          />
          <TextField
            label="Confirmar Contraseña"
            type="password"
            fullWidth
            value={confirmPassword}
            onChange={e => setConfirmPassword(e.target.value)}
            sx={{ mt: 2 }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setPasswordOpen(false)}>Cancelar</Button>
          <Button variant="contained" onClick={handlePasswordSave} disabled={loading}>
            {loading ? <CircularProgress size={20} /> : "Actualizar"}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
