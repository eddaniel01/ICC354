import { useEffect, useState } from "react";
import {
  Box, Typography, Table, TableBody, TableCell, TableHead, TableRow,
  Paper, Button, IconButton, TextField, Dialog, DialogActions, DialogContent,
  DialogTitle, CircularProgress, Stack, Tooltip} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";
import AddIcon from "@mui/icons-material/Add";
import VisibilityIcon from "@mui/icons-material/Visibility";
import UserDetailsDialog from "../components/UserDetailsDialog";
import { getAllUsers, createUser, updateUser, deleteUser, getUserById, User } from "../api/usersApi";

export default function AdminUsersPage() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [open, setOpen] = useState(false);
  const [editingUser, setEditingUser] = useState<User | null>(null);
  const [form, setForm] = useState({ username: "", role: "USER", password: "" });

  // Detalles
  const [detailOpen, setDetailOpen] = useState(false);
  const [userDetails, setUserDetails] = useState<User | null>(null);
  const [detailsLoading, setDetailsLoading] = useState(false);

  const fetchUsers = async () => {
    setLoading(true);
    const res = await getAllUsers();
    setUsers(res.data);
    setLoading(false);
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const handleOpen = (user?: User) => {
    if (user) {
      setEditingUser(user);
      setForm({ username: user.username, role: user.role, password: "" });
    } else {
      setEditingUser(null);
      setForm({ username: "", role: "USER", password: "" });
    }
    setOpen(true);
  };

  const handleClose = () => setOpen(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSave = async () => {
    if (editingUser) {
      const updatePayload: any = {
        username: form.username,
        role: form.role
      };
      if (form.password) {
        updatePayload.password = form.password;
      }
      await updateUser(editingUser.id, updatePayload);
    } else {
      await createUser(form);
    }
    handleClose();
    fetchUsers();
  };

  const handleDelete = async (id: string) => {
    await deleteUser(id);
    fetchUsers();
  };

  // ------ VER DETALLES ------
  const handleViewDetails = async (id: string) => {
    setDetailsLoading(true);
    setDetailOpen(true);
    try {
      const res = await getUserById(id);
      setUserDetails(res.data);
    } catch (err) {
      setUserDetails(null);
    }
    setDetailsLoading(false);
  };

  const handleDetailsClose = () => {
    setDetailOpen(false);
    setUserDetails(null);
  };

  return (
    <Box sx={{ maxWidth: 900, mx: "auto", mt: 6 }}>
      <Stack direction="row" alignItems="center" justifyContent="space-between" mb={3}>
        <Typography variant="h4" sx={{ fontWeight: 700, letterSpacing: -1 }}>Gestión de Usuarios</Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          sx={{
            borderRadius: 3,
            textTransform: "none",
            fontWeight: 500,
            boxShadow: "0 4px 20px 0 rgba(30,32,60,0.08)",
            bgcolor: "#1976d2",
            ":hover": { bgcolor: "#125ea9" }
          }}
          onClick={() => handleOpen()}
        >
          Nuevo Usuario
        </Button>
      </Stack>
      <Paper elevation={3} sx={{
        p: 2,
        borderRadius: 4,
        boxShadow: "0px 6px 32px 0px rgba(30,32,60,0.08)",
        bgcolor: "#fafbfc"
      }}>
        {loading ? (
          <Stack alignItems="center" py={4}>
            <CircularProgress />
          </Stack>
        ) : (
          <Table sx={{ minWidth: 650, borderRadius: 4, overflow: 'hidden' }}>
            <TableHead>
              <TableRow sx={{ bgcolor: "#e3e7ed" }}>
                <TableCell sx={{ fontWeight: "bold" }}>Usuario</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Rol</TableCell>
                <TableCell align="right" sx={{ fontWeight: "bold" }}>Acciones</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {users.map((user) => (
                <TableRow
                  key={user.id}
                  sx={{
                    transition: "background 0.2s",
                    "&:hover": { bgcolor: "#f1f6fa" }
                  }}
                >
                  <TableCell>{user.username}</TableCell>
                  <TableCell>
                    <Box
                      sx={{
                        px: 1.5,
                        py: 0.5,
                        borderRadius: 2,
                        bgcolor: user.role === "ADMIN" ? "#e3fcef" : "#e3e7ed",
                        color: user.role === "ADMIN" ? "#219653" : "#374151",
                        fontWeight: 500,
                        fontSize: "0.97em",
                        display: "inline-block"
                      }}
                    >
                      {user.role}
                    </Box>
                  </TableCell>
                  <TableCell align="right">
                    <Stack direction="row" spacing={1} justifyContent="flex-end">
                      <Tooltip title="Ver Detalles">
                        <IconButton color="primary" onClick={() => handleViewDetails(user.id)}>
                          <VisibilityIcon />
                        </IconButton>
                      </Tooltip>
                      <Tooltip title="Editar">
                        <IconButton color="secondary" onClick={() => handleOpen(user)}>
                          <EditIcon />
                        </IconButton>
                      </Tooltip>
                      <Tooltip title="Eliminar">
                        <IconButton color="error" onClick={() => handleDelete(user.id)}>
                          <DeleteIcon />
                        </IconButton>
                      </Tooltip>
                    </Stack>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        )}
      </Paper>
      {/* --- MODAL DETALLE --- */}
      <UserDetailsDialog
        open={detailOpen}
        onClose={handleDetailsClose}
        userDetails={userDetails}
        loading={detailsLoading}
      />
      {/* --- MODAL CREAR/EDITAR --- */}
      <Dialog open={open} onClose={handleClose} PaperProps={{ sx: { borderRadius: 4, minWidth: 340 } }}>
        <DialogTitle sx={{ bgcolor: "#1976d2", color: "#fff", pb: 2, borderTopLeftRadius: 16, borderTopRightRadius: 16 }}>
          <Stack direction="row" alignItems="center" spacing={1}>
            <AddIcon fontSize="small" />
            {editingUser ? "Editar Usuario" : "Nuevo Usuario"}
          </Stack>
        </DialogTitle>
        <DialogContent sx={{ mt: 1 }}>
          <TextField
            margin="dense"
            label="Usuario"
            name="username"
            value={form.username}
            onChange={handleChange}
            fullWidth
          />
          <TextField
            margin="dense"
            label="Rol"
            name="role"
            value={form.role}
            onChange={handleChange}
            select
            SelectProps={{ native: true }}
            fullWidth
          >
            <option value="USER">USER</option>
            <option value="ADMIN">ADMIN</option>
          </TextField>
          {!editingUser && (
            <TextField
              margin="dense"
              label="Contraseña"
              name="password"
              type="password"
              value={form.password}
              onChange={handleChange}
              fullWidth
            />
          )}
        </DialogContent>
        <DialogActions sx={{ pb: 2, px: 3 }}>
          <Button onClick={handleClose}>Cancelar</Button>
          <Button variant="contained" onClick={handleSave}>
            {editingUser ? "Actualizar" : "Crear"}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
