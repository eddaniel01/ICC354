import { useEffect, useState } from "react";
import { Book, getAllBooks } from "../api/catalogApi";
import { useCart } from "../contexts/CartContext"; // IMPORTANTE!
import {
  Grid,
  Button,
  Card,
  CardContent,
  Typography,
  CardActions,
  CircularProgress,
  Alert,
  CardMedia,
  Box,
  Pagination,
  Chip,
  TextField,
  InputAdornment,
  Snackbar
} from "@mui/material";
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import SearchIcon from '@mui/icons-material/Search';

export default function Catalog() {
  const [books, setBooks] = useState<Book[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [page, setPage] = useState(1);
  const [query, setQuery] = useState("");
  const [snackbar, setSnackbar] = useState<{ open: boolean, message: string }>({ open: false, message: "" });
  const booksPerPage = 6;

  const { addToCart } = useCart(); // USO DEL CONTEXTO!

  useEffect(() => {
    setLoading(true);
    getAllBooks()
      .then(setBooks)
      .catch(() => setError("Error cargando libros"))
      .finally(() => setLoading(false));
  }, []);

  const handleSearch = (event: React.ChangeEvent<HTMLInputElement>) => {
    setQuery(event.target.value);
    setPage(1);
  };

  // Filtrar libros por búsqueda
  const filteredBooks = books.filter(book =>
    book.title.toLowerCase().includes(query.toLowerCase()) ||
    book.author.toLowerCase().includes(query.toLowerCase())
  );

  const paginatedBooks = filteredBooks.slice(
    (page - 1) * booksPerPage,
    page * booksPerPage
  );

  // 🚀 NUEVO: Realmente agrega el libro al carrito
  const handleAddToCart = async (book: Book) => {
    try {
      await addToCart(book);
      setSnackbar({ open: true, message: `"${book.title}" agregado al carrito` });
    } catch {
      setSnackbar({ open: true, message: "Error al agregar al carrito" });
    }
  };

  const handleChangePage = (_: React.ChangeEvent<unknown>, value: number) => {
    setPage(value);
  };

  if (loading) return <CircularProgress sx={{ display: "block", mx: "auto", mt: 8 }} />;
  if (error) return <Alert severity="error">{error}</Alert>;

  return (
    <Box sx={{ px: { xs: 2, md: 12 }, py: 4, bgcolor: "#f7f7fa", minHeight: "100vh" }}>
      <Typography variant="h4" gutterBottom align="center" fontWeight={700}>
        Catálogo de Libros
      </Typography>
      <Box sx={{ display: "flex", justifyContent: "center", mb: 4 }}>
        <TextField
          placeholder="Buscar libros o autores..."
          variant="outlined"
          size="small"
          value={query}
          onChange={handleSearch}
          sx={{ width: 350, bgcolor: "#fff", borderRadius: 2, boxShadow: 1 }}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <SearchIcon color="action" />
              </InputAdornment>
            ),
          }}
        />
      </Box>
      <Grid container spacing={4}>
        {paginatedBooks.map((book) => (
          <Grid key={book.id} size={{ xs: 12, sm: 6, md: 4 }}>
            <Card
              sx={{
                borderRadius: 3,
                boxShadow: 3,
                height: "100%",
                display: "flex",
                flexDirection: "column",
                transition: "box-shadow 0.3s, transform 0.2s",
                bgcolor: "#fff",
                ":hover": { boxShadow: 8, transform: "translateY(-5px) scale(1.02)" }
              }}
            >
              <CardMedia
                component="img"
                height="180"
                image={`https://picsum.photos/seed/${book.id}/350/180`}
                alt={book.title}
                sx={{ borderTopLeftRadius: 12, borderTopRightRadius: 12, objectFit: "cover" }}
              />
              <CardContent sx={{ flexGrow: 1 }}>
                <Typography variant="h6" sx={{ mb: 1 }}>{book.title}</Typography>
                <Typography color="text.secondary" sx={{ fontSize: 14, mb: 1 }}>
                  {book.author}
                </Typography>
                <Chip label={book.genre} size="small" color="secondary" sx={{ mb: 1 }} />
                <Typography sx={{ fontSize: 18, fontWeight: 700, color: "success.dark" }}>
                  ${book.price.toFixed(2)}
                </Typography>
              </CardContent>
              <CardActions sx={{ p: 2, pt: 0 }}>
                <Button
                  fullWidth
                  variant="contained"
                  size="medium"
                  sx={{ borderRadius: 2, fontWeight: 600 }}
                  startIcon={<ShoppingCartIcon />}
                  onClick={() => handleAddToCart(book)}
                >
                  Agregar al carrito
                </Button>
              </CardActions>
            </Card>
          </Grid>
        ))}
      </Grid>
      {filteredBooks.length > booksPerPage && (
        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
          <Pagination
            count={Math.ceil(filteredBooks.length / booksPerPage)}
            page={page}
            onChange={handleChangePage}
            color="primary"
            shape="rounded"
          />
        </Box>
      )}
      {filteredBooks.length === 0 && (
        <Typography align="center" sx={{ mt: 6, color: "grey.600" }}>
          No se encontraron libros.
        </Typography>
      )}
      <Snackbar
        open={snackbar.open}
        autoHideDuration={2000}
        onClose={() => setSnackbar({ open: false, message: "" })}
        message={snackbar.message}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      />
    </Box>
  );
}
