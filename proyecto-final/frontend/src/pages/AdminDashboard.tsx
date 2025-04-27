import { useEffect, useState } from "react";
import { Box, Typography, Button, Stack, Paper, Grid, Card, CardContent, CircularProgress } from "@mui/material";
import PeopleAltIcon from '@mui/icons-material/PeopleAlt';
import MenuBookIcon from '@mui/icons-material/MenuBook';
import { useNavigate } from "react-router-dom";
import { getAllUsers } from "../api/usersApi";
// import { getAllBooks } from "../api/catalogApi";

export default function AdminDashboard() {
  const navigate = useNavigate();
  const [totalUsuarios, setTotalUsuarios] = useState<number | null>(null);
  // const [totalLibros, setTotalLibros] = useState<number | null>(null);

  useEffect(() => {
    getAllUsers().then(res => setTotalUsuarios(res.data.length));
    // getAllBooks().then(res => setTotalLibros(res.data.length));
  }, []);

  return (
    <Box
      sx={{
        maxWidth: 1000,
        mx: "auto",
        mt: 6,
        p: 2,
        bgcolor: "#f5f6fa",
        minHeight: "100vh",
      }}
    >
      <Typography variant="h4" fontWeight="bold" mb={4} color="primary">
        Panel de Administrador
      </Typography>

      <Grid container spacing={3} mb={4}>
        <Grid size={{ xs: 12, md: 6 }}>
          <Card sx={{ display: "flex", alignItems: "center", p: 2, borderLeft: "5px solid #1976d2" }}>
            <PeopleAltIcon sx={{ fontSize: 50, color: "#1976d2", mr: 2 }} />
            <CardContent sx={{ flexGrow: 1 }}>
              <Typography variant="h6">Usuarios Registrados</Typography>
              <Typography variant="h3" fontWeight="bold">
                {totalUsuarios !== null ? totalUsuarios : <CircularProgress size={24} />}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid size={{ xs: 12, md: 6 }}>
          <Card sx={{ display: "flex", alignItems: "center", p: 2, borderLeft: "5px solid #43a047" }}>
            <MenuBookIcon sx={{ fontSize: 50, color: "#43a047", mr: 2 }} />
            <CardContent sx={{ flexGrow: 1 }}>
              <Typography variant="h6">Libros en Catálogo</Typography>
              <Typography variant="h3" fontWeight="bold">
                {/* totalLibros !== null ? totalLibros : <CircularProgress size={24} /> */}
                (próximamente)
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      <Paper elevation={2} sx={{ p: 3, mb: 4, borderRadius: 3, bgcolor: "#fff" }}>
        <Typography variant="body1" mb={3} color="text.secondary">
          ¡Bienvenido/a al panel de administración! Aquí puedes gestionar usuarios, ver estadísticas y administrar el sistema.
        </Typography>
        <Stack spacing={2} direction={{ xs: "column", sm: "row" }}>
          <Button
            variant="contained"
            color="primary"
            size="large"
            onClick={() => navigate("/admin/users")}
            sx={{ fontWeight: "bold", px: 4, borderRadius: 2 }}
          >
            Gestionar Usuarios
          </Button>
        </Stack>
      </Paper>
    </Box>
  );
}
