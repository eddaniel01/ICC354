import { useEffect, useState } from "react";
import { useAuth } from "../contexts/AuthContext";
import { getOrdersByUsername, Order } from "../api/ordersApi";
import {
  Box, Typography, Paper, Table, TableHead, TableRow, TableCell, TableBody, CircularProgress, Chip, Stack
} from "@mui/material";
import ReceiptLongIcon from "@mui/icons-material/ReceiptLong";

export default function OrderHistory() {
  const { user } = useAuth();
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!user) return;
    setLoading(true);
    getOrdersByUsername(user.username)
      .then(res => setOrders(res.data))
      .finally(() => setLoading(false));
  }, [user]);

  return (
    <Box sx={{ maxWidth: 700, mx: "auto", mt: 5 }}>
      <Paper sx={{ p: 4, borderRadius: 4, boxShadow: 6, bgcolor: "#fafafa" }}>
        <Stack direction="row" alignItems="center" spacing={2} mb={2}>
          <ReceiptLongIcon color="primary" fontSize="large" />
          <Typography variant="h5" fontWeight={700}>
            Historial de Compras
          </Typography>
        </Stack>
        {loading ? (
          <Box display="flex" justifyContent="center" py={6}><CircularProgress /></Box>
        ) : orders.length === 0 ? (
          <Typography color="text.secondary" mt={3} align="center">
            No tienes compras registradas.
          </Typography>
        ) : (
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>ID Orden</TableCell>
                <TableCell>Fecha</TableCell>
                <TableCell align="right">Total</TableCell>
                {/* Puedes agregar más columnas si quieres */}
              </TableRow>
            </TableHead>
            <TableBody>
              {orders.map(order => (
                <TableRow key={order.id} hover>
                  <TableCell>
                    <Chip
                      label={`#${order.id}`}
                      color="primary"
                      variant="outlined"
                      size="small"
                    />
                  </TableCell>
                  <TableCell>
                    {new Date(order.date).toLocaleString("es-ES")}
                  </TableCell>
                  <TableCell align="right">
                    <Typography fontWeight={700} color="success.main">
                      ${order.totalAmount.toFixed(2)}
                    </Typography>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        )}
      </Paper>
    </Box>
  );
}
