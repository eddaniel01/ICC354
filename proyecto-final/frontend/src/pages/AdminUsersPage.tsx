import { useEffect, useState } from "react";
import { getAllUsers, createUser, updateUser, deleteUser, User } from "../api/usersApi";
import {
  Box, Typography, Table, TableBody, TableCell, TableHead, TableRow,
  Paper, Button, IconButton, TextField, Dialog, DialogActions, DialogContent,
  DialogTitle, CircularProgress, Stack
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";
import AddIcon from "@mui/icons-material/Add";

export default function AdminUsersPage() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [open, setOpen] = useState(false);
  const [editingUser, setEditingUser] = useState<User | null>(null);
  const [form, setForm] = useState({ username: "", role: "USER", password: "" });

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
      // Construye el payload sin password si está vacía
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

  return (
    <Box sx={{ maxWidth: 800, mx: "auto", mt: 6 }}>
      <Stack direction="row" alignItems="center" justifyContent="space-between" mb={3}>
        <Typography variant="h4">Gestión de Usuarios</Typography>
        <Button variant="contained" startIcon={<AddIcon />} onClick={() => handleOpen()}>
          Nuevo Usuario
        </Button>
      </Stack>
      <Paper sx={{ p: 2 }}>
        {loading ? (
          <CircularProgress />
        ) : (
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Usuario</TableCell>
                <TableCell>Rol</TableCell>
                <TableCell align="right">Acciones</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {users.map((user) => (
                <TableRow key={user.id}>
                  <TableCell>{user.username}</TableCell>
                  <TableCell>{user.role}</TableCell>
                  <TableCell align="right">
                    <IconButton onClick={() => handleOpen(user)}><EditIcon /></IconButton>
                    <IconButton color="error" onClick={() => handleDelete(user.id)}><DeleteIcon /></IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        )}
      </Paper>
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>{editingUser ? "Editar Usuario" : "Nuevo Usuario"}</DialogTitle>
        <DialogContent sx={{ minWidth: 300 }}>
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
        <DialogActions>
          <Button onClick={handleClose}>Cancelar</Button>
          <Button onClick={handleSave} variant="contained">
            {editingUser ? "Actualizar" : "Crear"}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
