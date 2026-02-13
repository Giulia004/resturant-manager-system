// src/App.tsx
import React, { useState } from "react";
import { register, login, logout, type AuthResponse } from "./api/auth/authService";
import Home from "./components/Home";
import Menu from "./components/Menu"; // Assicurati che il percorso sia corretto

function App() {
  const [username, setUsername] = useState("");
  const [psw, setPsw] = useState("");
  const [message, setMessage] = useState("");
  const [isLoginView, setIsLoginView] = useState(true);

  // Gestione della vista: 'login', 'home' o 'menu'
  const [view, setView] = useState<"login" | "home" | "menu">(
    localStorage.getItem("token") ? "home" : "login"
  );

  const handleAction = async () => {
    try {
      const response: AuthResponse = isLoginView
        ? await login({ username, psw })
        : await register({ username, psw });

      if (response.token) {
        setView("home"); // Una volta loggato, vai alla Home
        setMessage("");
      } else {
        setMessage("Credenziali non valide o errore server.");
      }
    } catch (err) {
      setMessage("Errore critico di connessione.");
    }
  };

  const handleLogout = () => {
    logout();
    setView("login");
    setUsername("");
    setPsw("");
  };

  // Recuperiamo il token per passarlo al Menu
  const token = localStorage.getItem("token") || "";

  // --- RENDERING CONDIZIONALE ---

  // 1. Schermata MENU
  if (view === "menu") {
    return (
      <div style={styles.fullPageContainer}>
        {/* Usiamo una card più larga per il menu */}
        <div style={{ ...styles.card, maxWidth: "800px", width: "95%" }}>
          <Menu token={token} onBack={() => setView("home")} />
        </div>
      </div>
    );
  }

  // 2. Schermata HOME
  if (view === "home") {
    return (
      <Home
        onLogout={handleLogout}
        onPizzaClick={() => setView("menu")}
      />
    );
  }

  // 3. Schermata LOGIN (default)
  return (
    <div style={styles.fullPageContainer}>
      <div style={styles.card}>
        <div style={styles.iconCircle}>🍕</div>
        <h2 style={styles.title}>{isLoginView ? "Ristorante Login" : "Nuovo Account"}</h2>

        <div style={styles.inputGroup}>
          <input
            type="text"
            placeholder="Username"
            style={styles.input}
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <input
            type="password"
            placeholder="Password"
            style={styles.input}
            value={psw}
            onChange={(e) => setPsw(e.target.value)}
          />
        </div>

        <button onClick={handleAction} style={styles.primaryButton}>
          {isLoginView ? "ACCEDI" : "REGISTRATI"}
        </button>

        <div style={styles.switchText}>
          {isLoginView ? "Ancora non sei dei nostri?" : "Hai già un account?"}
          <span onClick={() => setIsLoginView(!isLoginView)} style={styles.link}>
            {isLoginView ? " Registrati" : " Accedi"}
          </span>
        </div>

        {message && <div style={styles.message}>{message}</div>}
      </div>
    </div>
  );
}

const styles: { [key: string]: React.CSSProperties } = {

  fullPageContainer: {
    position: "fixed",
    top: 0,
    left: 0,
    width: "100vw",
    height: "100vh",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    background: "linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%)",
    fontFamily: "'Poppins', sans-serif",
  },

  card: {
    background: "#ffffff",
    padding: "50px 40px",
    borderRadius: "28px",
    width: "380px",
    boxShadow: "0 20px 50px rgba(0,0,0,0.15)",
    textAlign: "center",
    transition: "transform 0.3s ease",
  },

  iconCircle: {
    width: "70px",
    height: "70px",
    margin: "0 auto 20px auto",
    borderRadius: "50%",
    background: "linear-gradient(135deg, #ff9966, #ff5e62)",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    fontSize: "2rem",
    color: "#fff",
    boxShadow: "0 10px 25px rgba(255,94,98,0.4)",
  },

  title: {
    fontSize: "1.8rem",
    fontWeight: 700,
    marginBottom: "30px",
    color: "#222",
  },

  inputGroup: {
    display: "flex",
    flexDirection: "column",
    gap: "15px",
    marginBottom: "25px",
  },

  input: {
    padding: "14px 18px",
    borderRadius: "14px",
    border: "1px solid #ddd",
    fontSize: "0.95rem",
    outline: "none",
    transition: "all 0.3s ease",
  },

  primaryButton: {
    width: "100%",
    padding: "14px",
    borderRadius: "14px",
    border: "none",
    background: "linear-gradient(135deg, #ff5e62, #ff9966)",
    color: "#fff",
    fontWeight: 700,
    fontSize: "0.95rem",
    cursor: "pointer",
    boxShadow: "0 10px 25px rgba(255,94,98,0.4)",
    transition: "all 0.3s ease",
  },

  switchText: {
    marginTop: "20px",
    fontSize: "0.85rem",
    color: "#555",
  },

  link: {
    marginLeft: "5px",
    color: "#ff5e62",
    fontWeight: 600,
    cursor: "pointer",
  },

  message: {
    marginTop: "20px",
    padding: "10px",
    borderRadius: "10px",
    backgroundColor: "#ffe6e6",
    color: "#d63031",
    fontSize: "0.85rem",
    fontWeight: 600,
  },
};
export default App;