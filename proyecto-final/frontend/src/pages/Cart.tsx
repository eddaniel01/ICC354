import { useCart } from "../contexts/CartContext";
import { useAuth } from "../contexts/AuthContext";
import { createOrder } from "../api/ordersApi";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import DeleteIcon from "@mui/icons-material/Delete";
import RemoveIcon from "@mui/icons-material/Remove";
import AddIcon from "@mui/icons-material/Add";
import CreditCardIcon from "@mui/icons-material/CreditCard";
import {
  Box, Typography, Card, CardContent, List,
  ListItem, IconButton, Button, Divider, Stack,
  Dialog, DialogTitle, DialogContent, DialogActions, TextField, CircularProgress, Fade
} from "@mui/material";
import { useState } from "react";

export default function Cart() {
  const { cart, removeFromCart, clearCart, increaseQuantity, decreaseQuantity } = useCart();
  const { user } = useAuth();
  const [loading, setLoading] = useState(false);

  // Estado para el modal de pago simulado
  const [payOpen, setPayOpen] = useState(false);
  const [cardNumber, setCardNumber] = useState("");
  const [cardName, setCardName] = useState("");
  const [cardError, setCardError] = useState("");

  const total = cart.reduce((acc, item) => acc + item.price * (item.quantity ?? 1), 0);

  // Al hacer checkout, abre el modal de pago
  const handleCheckout = () => {
    setCardNumber("");
    setCardName("");
    setCardError("");
    setPayOpen(true);
  };

  // Simula el pago y registra la orden
  const handlePay = async () => {
    if (!cardNumber || !cardName) {
      setCardError("Completa todos los campos de la tarjeta.");
      return;
    }
    setLoading(true);
    setCardError("");
    try {
      await createOrder(user!.username);
      await clearCart();
      setPayOpen(false);
      alert("¡Compra registrada con éxito! (Pago simulado)");
    } catch (e) {
      setCardError("No se pudo registrar la compra.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ mt: { xs: 3, md: 0 }, mx: { xs: 0, md: 3 } }}>
      <Card sx={{ maxWidth: 340, mx: "auto", borderRadius: 3, boxShadow: 6, bgcolor: "#fff", p: 2 }}>
        <CardContent>
          <Stack direction="row" alignItems="center" spacing={1} mb={2}>
            <ShoppingCartIcon color="primary" />
            <Typography variant="h5" fontWeight={700}>Carrito de Compras</Typography>
          </Stack>
          <Divider sx={{ mb: 2 }} />
          <List>
            {cart.length === 0 && (
              <Typography align="center" color="text.secondary" sx={{ py: 3 }}>
                El carrito está vacío.
              </Typography>
            )}
            {cart.map(item => (
              <ListItem
                key={item.bookId}
                alignItems="flex-start"
                sx={{ borderRadius: 2, mb: 2, px: 2, py: 2, bgcolor: "#f5f5f7", boxShadow: 1 }}
                secondaryAction={
                  <IconButton edge="end" aria-label="eliminar" color="error" onClick={() => removeFromCart(item.id)}>
                    <DeleteIcon />
                  </IconButton>
                }
              >
                {/* Imagen a la izquierda */}
                <Box sx={{ minWidth: 72, mr: 2 }}>
                  <img
                    src={`https://picsum.photos/seed/${item.bookId}/90/90`}
                    alt={item.title}
                    style={{ borderRadius: 10, width: 72, height: 72, objectFit: "cover" }}
                  />
                </Box>
                {/* Detalles */}
                <Box flex={1}>
                  <Typography fontWeight={600} fontSize={17} mb={0.5}>{item.title}</Typography>
                  <Typography color="text.secondary" fontSize={15}>{item.author}</Typography>
                  <Box display="flex" alignItems="center" mt={1}>
                    <Typography fontWeight={700} color="success.main" fontSize={16}>
                      ${item.price.toFixed(2)}
                    </Typography>
                    <Box ml={2} display="flex" alignItems="center">
                      <IconButton size="small" onClick={() => decreaseQuantity(item.bookId)} disabled={item.quantity <= 1}>
                        <RemoveIcon />
                      </IconButton>
                      <Typography fontWeight={600} mx={1}>
                        {item.quantity}
                      </Typography>
                      <IconButton size="small" onClick={() => increaseQuantity(item.bookId)}>
                        <AddIcon />
                      </IconButton>
                    </Box>
                  </Box>
                </Box>
              </ListItem>
            ))}
          </List>
          <Divider sx={{ my: 2 }} />
          <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
            <Typography variant="subtitle1" fontWeight={600}>Total</Typography>
            <Typography variant="h6" color="success.main" fontWeight={700}>
              ${total.toFixed(2)}
            </Typography>
          </Box>
          <Button
            variant="contained"
            color="primary"
            fullWidth
            size="large"
            onClick={handleCheckout}
            sx={{ fontWeight: 600, borderRadius: 2, mb: 1 }}
            disabled={cart.length === 0 || loading}
          >
            {loading ? "Procesando..." : "Finalizar compra"}
          </Button>
          <Button
            variant="outlined"
            color="error"
            fullWidth
            size="small"
            onClick={clearCart}
            sx={{ fontWeight: 600, borderRadius: 2 }}
            disabled={cart.length === 0}
          >
            Vaciar carrito
          </Button>
        </CardContent>
      </Card>

      {/* MODAL DE PAGO SIMULADO */}
      <Dialog open={payOpen} onClose={() => setPayOpen(false)} PaperProps={{ sx: { borderRadius: 3 } }}>
        <DialogTitle>
          <CreditCardIcon sx={{ mr: 1, color: "#1976d2" }} />
          Pago con Tarjeta (Simulado)
        </DialogTitle>
        <DialogContent>
          <TextField
            label="Nombre en la tarjeta"
            fullWidth
            value={cardName}
            onChange={e => setCardName(e.target.value)}
            sx={{ mt: 1, mb: 2 }}
          />
          <TextField
            label="Número de tarjeta"
            fullWidth
            value={cardNumber}
            onChange={e => setCardNumber(e.target.value)}
            sx={{ mb: 2 }}
            inputProps={{ maxLength: 16 }}
            placeholder="XXXX XXXX XXXX XXXX"
          />
          {cardError && (
            <Fade in>
              <Typography color="error" fontSize={15}>{cardError}</Typography>
            </Fade>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setPayOpen(false)} disabled={loading}>Cancelar</Button>
          <Button
            variant="contained"
            color="success"
            onClick={handlePay}
            disabled={loading}
            startIcon={loading ? <CircularProgress color="inherit" size={18} /> : null}
          >
            {loading ? "Procesando..." : "Pagar"}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
