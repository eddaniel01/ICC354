import { Dialog, DialogTitle, DialogContent, DialogActions, Typography, Button, Stack, Chip } from "@mui/material";
import { Book } from "../api/catalogApi";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";

type Props = {
  book: Book | null;
  open: boolean;
  onClose: () => void;
  onAddToCart?: (book: Book) => void;
};

export default function BookDetailModal({ book, open, onClose, onAddToCart }: Props) {
  if (!book) return null;
  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>Detalles del Libro</DialogTitle>
      <DialogContent>
        <Stack spacing={2} alignItems="center" sx={{ mt: 1 }}>
          <img
            src={`https://picsum.photos/seed/${book.id}/180/250`}
            alt={book.title}
            style={{ borderRadius: 8, boxShadow: "0 2px 12px #0002" }}
          />
          <Typography variant="h5" fontWeight={700}>{book.title}</Typography>
          <Typography color="text.secondary">Autor: {book.author}</Typography>
          <Chip label={book.genre} color="secondary" />
          <Typography color="success.main" fontWeight={600} fontSize={22}>${book.price.toFixed(2)}</Typography>
        </Stack>
      </DialogContent>
      <DialogActions>
        <Button
          onClick={() => onAddToCart && onAddToCart(book)}
          variant="contained"
          color="primary"
          startIcon={<ShoppingCartIcon />}
        >
          Agregar al carrito
        </Button>
        <Button onClick={onClose} color="primary" variant="outlined">
          Cerrar
        </Button>
      </DialogActions>
    </Dialog>
  );
}
