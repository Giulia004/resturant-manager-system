// src/components/LoginForm.tsx
import React, { useState } from "react";
import { login } from "../api/auth/authService";

export const LoginForm = () => {
  const [username, setUsername] = useState("");
  const [psw, setPsw] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setMessage("");

    try {
      const response = await login({ username, psw });

      if (response.token) {
        localStorage.setItem("token", response.token);
        setMessage("Login effettuato con successo!");
      } else {
        setMessage("Credenziali errate");
      }
    } catch (err) {
      setMessage("Errore durante il login");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.page}>
      <div style={styles.card}>
        <div style={styles.iconCircle}>🍕</div>
        <h2 style={styles.title}>Bentornato</h2>
        <p style={styles.subtitle}>Accedi al gestionale</p>

        <form onSubmit={handleSubmit} style={styles.form}>
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            style={styles.input}
          />

          <input
            type="password"
            placeholder="Password"
            value={psw}
            onChange={(e) => setPsw(e.target.value)}
            style={styles.input}
          />

          <button type="submit" style={styles.button} disabled={loading}>
            {loading ? "Accesso..." : "ACCEDI"}
          </button>
        </form>

        {message && (
          <p
            style={{
              ...styles.message,
              color: message.includes("successo") ? "#27ae60" : "#e74c3c"
            }}
          >
            {message}
          </p>
        )}
      </div>
    </div>
  );
};

const styles: { [key: string]: React.CSSProperties } = {
  page: {
    minHeight: "100vh",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    background: "linear-gradient(135deg, #fff8f0 0%, #ffeacc 100%)",
    fontFamily: "'Poppins', sans-serif",
  },

  card: {
    backgroundColor: "#fff",
    padding: "40px 30px",
    borderRadius: "20px",
    width: "100%",
    maxWidth: "400px",
    boxShadow: "0 20px 40px rgba(230, 126, 34, 0.1)",
    textAlign: "center",
    border: "1px solid rgba(230, 126, 34, 0.05)"
  },

  iconCircle: {
    width: "70px",
    height: "70px",
    backgroundColor: "#fff3e0",
    borderRadius: "50%",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    fontSize: "32px",
    margin: "0 auto 20px auto",
    boxShadow: "0 4px 15px rgba(230, 126, 34, 0.15)"
  },

  title: {
    margin: "0 0 5px 0",
    fontSize: "1.8rem",
    fontWeight: 800,
    color: "#2c3e50",
  },

  subtitle: {
    margin: "0 0 30px 0",
    color: "#7f8c8d",
    fontSize: "0.95rem"
  },

  form: {
    display: "flex",
    flexDirection: "column",
    gap: "20px",
  },

  input: {
    padding: "15px",
    borderRadius: "12px",
    border: "2px solid #f0f0f0",
    fontSize: "1rem",
    outline: "none",
    transition: "all 0.2s ease",
    backgroundColor: "#f9f9f9",
    color: "#2c3e50"
  },

  button: {
    marginTop: "10px",
    padding: "15px",
    borderRadius: "12px",
    border: "none",
    backgroundColor: "#e67e22",
    color: "#fff",
    fontWeight: 800,
    fontSize: "1rem",
    cursor: "pointer",
    transition: "all 0.3s ease",
    boxShadow: "0 4px 15px rgba(230, 126, 34, 0.3)"
  },

  message: {
    marginTop: "15px",
    fontWeight: 600,
    fontSize: "0.9rem",
  },
};