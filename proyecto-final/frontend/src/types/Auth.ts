export type AuthResponse = {
  id: string | number;
  email: string;
  token: string;
  username: string;
  role: string; // o roles: string[] si devuelves array en backend
};
export type AuthContextType = {
  user: AuthResponse | null;
  login: (user: AuthResponse) => void;
  logout: () => void;
  isAuthenticated: () => boolean;
};