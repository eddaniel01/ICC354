// src/components/UserDetailsDialog.tsx

import { Avatar, Box, Typography, Dialog, DialogContent, DialogTitle, DialogActions, Button, Stack, Divider } from "@mui/material";
import PersonIcon from "@mui/icons-material/Person";
import BadgeIcon from "@mui/icons-material/Badge";
import AdminPanelSettingsIcon from "@mui/icons-material/AdminPanelSettings";
import { User } from "../api/usersApi"; // <-- Ajusta el path si es necesario

interface UserDetailsDialogProps {
  open: boolean;
  onClose: () => void;
  userDetails: User | null;
  loading: boolean;
}

export default function UserDetailsDialog({
  open,
  onClose,
  userDetails,
  loading
}: UserDetailsDialogProps) {
  return (
    <Dialog open={open} onClose={onClose} PaperProps={{ sx: { borderRadius: 4, minWidth: 350, boxShadow: 10 } }}>
      <DialogTitle>
        <Stack direction="row" alignItems="center" spacing={2}>
          <Avatar sx={{ bgcolor: "#1976d2", width: 54, height: 54 }}>
            <PersonIcon fontSize="large" />
          </Avatar>
          <Box>
            <Typography variant="h6">Detalles de Usuario</Typography>
            <Typography variant="caption" color="text.secondary">
              Información del usuario
            </Typography>
          </Box>
        </Stack>
      </DialogTitle>
      <Divider />
      <DialogContent>
        {loading ? (
          <Typography align="center" mt={2}>Cargando...</Typography>
        ) : userDetails ? (
          <Stack spacing={2} mt={1}>
            <Stack direction="row" alignItems="center" spacing={1}>
              <BadgeIcon color="primary" />
              <Typography variant="subtitle2" fontWeight={600}>ID:</Typography>
              <Typography variant="body2">{userDetails.id}</Typography>
            </Stack>
            <Stack direction="row" alignItems="center" spacing={1}>
              <PersonIcon color="primary" />
              <Typography variant="subtitle2" fontWeight={600}>Usuario:</Typography>
              <Typography variant="body2">{userDetails.username}</Typography>
            </Stack>
            <Stack direction="row" alignItems="center" spacing={1}>
              <AdminPanelSettingsIcon color={userDetails.role === "ADMIN" ? "success" : "action"} />
              <Typography variant="subtitle2" fontWeight={600}>Rol:</Typography>
              <Typography variant="body2">{userDetails.role}</Typography>
            </Stack>
          </Stack>
        ) : (
          <Typography color="error">No se pudo cargar el usuario</Typography>
        )}
      </DialogContent>
      <DialogActions>
        <Button variant="contained" onClick={onClose} fullWidth sx={{ borderRadius: 3 }}>
          Cerrar
        </Button>
      </DialogActions>
    </Dialog>
  );
}
