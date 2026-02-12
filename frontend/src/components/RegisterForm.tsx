// src/components/RegisterForm.tsx
import { useState } from "react";
import { register } from "../api/auth/authService";

export const RegisterForm = () => {
  const [username, setUsername] = useState("");
  const [psw, setPsw] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await register({ username, psw });
      if (response.token) {
        setMessage(`Registrato! Token: ${response.token}`);
        localStorage.setItem("token", response.token);
      } else {
        setMessage(response.token || "Errore nel registro");
      }
    } catch (err) {
      setMessage("Errore durante la registrazione");
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
      <button type="submit">Register</button>
      <p>{message}</p>
    </form>
  );
};