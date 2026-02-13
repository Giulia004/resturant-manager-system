// src/components/LoginForm.tsx
import { useState } from "react";
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
        <div style={styles.icon}>🍕</div>

        <h2 style={styles.title}>Ristorante Login</h2>

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
    background: "linear-gradient(135deg, #fff3e6, #ffe0cc)",
    fontFamily: "'Poppins', sans-serif",
  },

  card: {
    backgroundColor: "#fff",
    padding: "40px",
    borderRadius: "24px",
    width: "350px",
    boxShadow: "0 15px 35px rgba(0,0,0,0.1)",
    textAlign: "center",
  },

  icon: {
    fontSize: "3rem",
    marginBottom: "10px",
  },

  title: {
    marginBottom: "25px",
    fontSize: "1.8rem",
    fontWeight: 700,
    color: "#1a1a1a",
  },

  form: {
    display: "flex",
    flexDirection: "column",
    gap: "15px",
  },

  input: {
    padding: "12px 15px",
    borderRadius: "12px",
    border: "1px solid #ddd",
    fontSize: "0.9rem",
    outline: "none",
    transition: "all 0.2s ease",
  },

  button: {
    marginTop: "10px",
    padding: "12px",
    borderRadius: "12px",
    border: "none",
    backgroundColor: "#e67e22",
    color: "#fff",
    fontWeight: 700,
    cursor: "pointer",
    transition: "all 0.3s ease",
  },

  message: {
    marginTop: "15px",
    fontWeight: 600,
    fontSize: "0.9rem",
  },
};