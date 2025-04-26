import { useCart } from "../contexts/CartContext";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import DeleteIcon from "@mui/icons-material/Delete";
import RemoveIcon from "@mui/icons-material/Remove";
import AddIcon from "@mui/icons-material/Add";
import {
  Box, Typography, Card, CardContent, List,
  ListItem, IconButton, Button, Divider, Stack
} from "@mui/material";

export default function Cart() {
  const { cart, removeFromCart, clearCart, increaseQuantity, decreaseQuantity } = useCart();

  const total = cart.reduce((acc, item) => acc + item.price * (item.quantity ?? 1), 0);

  const handleCheckout = () => {
    alert("Funcionalidad de pago pendiente...");
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
            disabled={cart.length === 0}
          >
            Finalizar compra
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
    </Box>
  );
}
