import { Navigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { JSX } from "react";

type ProtectedRouteProps = {
  children: JSX.Element;
  allowedRoles?: string[]; // Si no se pasa, cualquier logueado entra
};

export default function ProtectedRoute({ children, allowedRoles }: ProtectedRouteProps) {
  const { user } = useAuth();

  if (!user) {
    // No logueado, redirige a login
    return <Navigate to="/login" />;
  }

  // Si tienes roles tipo string o array, ajusta esto:
  const userRoles = Array.isArray(user.roles) ? user.roles : [user.roles];
  if (allowedRoles && !allowedRoles.some((role) => userRoles.includes(role))) {
    // No tiene permiso, redirige a Home o NotFound
    return <Navigate to="/" />;
  }

  return children;
}
