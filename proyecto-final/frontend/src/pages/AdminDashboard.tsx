import { Box, Typography, Button, Stack, Paper } from "@mui/material";
import { useNavigate } from "react-router-dom";

export default function AdminDashboard() {
  const navigate = useNavigate();

  return (
    <Box sx={{ maxWidth: 800, mx: "auto", mt: 6 }}>
      <Typography variant="h4" mb={3}>Panel de Administrador</Typography>
      <Paper sx={{ p: 3, mb: 4 }}>
        <Typography mb={2}>
          ¡Bienvenido/a al panel de administración! Aquí puedes gestionar usuarios, ver estadísticas y administrar el sistema.
        </Typography>
        <Stack spacing={2} direction={{ xs: "column", sm: "row" }}>
          <Button
            variant="contained"
            color="primary"
            onClick={() => navigate("/admin/users")}
          >
            Gestionar Usuarios
          </Button>
        </Stack>
      </Paper>
      <Paper sx={{ p: 3 }}>
        <Typography variant="h6" gutterBottom>Estadísticas rápidas</Typography>
        <ul>
          <li>Total de usuarios registrados: (aquí puedes mostrarlo dinámicamente)</li>
          <li>Libros en catálogo: (puedes traerlo del backend)</li>
        </ul>
      </Paper>
    </Box>
  );
}
