// src/components/LoginForm.tsx
import { useState } from "react";
import { login } from "../api/auth/authService";

export const LoginForm = () => {
  const [username, setUsername] = useState("");
  const [psw, setPsw] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await login({ username, psw });
      if (response.token) {
        setMessage(`Login OK! Token: ${response.token}`);
        localStorage.setItem("token", response.token);
      } else {
        setMessage("Credenziali errate");
      }
    } catch (err) {
      setMessage("Errore durante il login");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="text"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
      <input
        type="password"
        placeholder="Password"
        value={psw}
        onChange={(e) => setPsw(e.target.value)}
      />
      <button type="submit">Login</button>
      <p>{message}</p>
    </form>
  );
};