// src/App.tsx
import React, { useState } from "react";
import { login, logout, type AuthResponse } from "./api/auth/authService";
import Menu from "./components/Menu";
import Dashboard from "./pages/Dashboard";
import ControlPanel from "./pages/ControlPanel";

function App() {
  const [username, setUsername] = useState("");
  const [psw, setPsw] = useState("");
  const [message, setMessage] = useState("");

  // Gestione della view
  const [view, setView] = useState<"login" | "home" | "menu" | "controlpanel">(
    localStorage.getItem("token") ? (localStorage.getItem("role") === "USER" ? "menu" : "home") : "login"
  );

  const handleAction = async () => {
    try {
      const response: AuthResponse = await login({ username, psw });
      if (response.token) {
        localStorage.setItem("token", response.token);
        if (response.role)
          localStorage.setItem("role", response.role);
        if ((response as any).tableNumber) {
          localStorage.setItem("tableNumber", (response as any).tableNumber);
        }
        if (response.role === "USER") {
          setView("menu");
        } else {
          setView("home");
        }
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
  const tableNumber = localStorage.getItem("tableNumber");

  // --- RENDERING CONDIZIONALE ---

  // 1. Schermata MENU
  if (view === "menu") {
    return (
      <div style={styles.fullPageContainer}>
        <Menu
          token={token}
          tableNumber={tableNumber ? parseInt(tableNumber) : undefined}
          onBack={() => setView("home")}
        />
      </div>
    );
  }

  // 2. Schermata HOME
  if (view === "home") {
    return (
      <Dashboard
        onLogout={handleLogout}
        onMenuClick={() => setView("menu")}
        onControlPanelClick={() => setView("controlpanel")}
      />
    );
  }

  if (view === "controlpanel") {
    return (
      <div style={styles.fullPageContainer}>
        <div style={styles.panelContainer}>
          <ControlPanel onBack={() => setView("home")} />
        </div>
      </div>
    );
  }
  // 3. Schermata LOGIN
  return (
    <div style={styles.fullPageContainer}>
      <div style={styles.card}>
        <div style={styles.iconCircle}>🍕</div>
        <h2 style={styles.title}>Ristorante Login</h2>

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
          ACCEDI
        </button>

        {message && <div style={styles.message}>{message}</div>}
      </div>
    </div>
  );
}

const styles: { [key: string]: React.CSSProperties } = {

  fullPageContainer: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    minHeight: "100vh",
    width: "100%",
    background: "linear-gradient(135deg, #e67e22 0%, #f39c12 100%)",
    fontFamily: "'Poppins', sans-serif",
    padding: "20px",
  },

  card: {
    background: "rgba(255, 255, 255, 0.85)",
    backdropFilter: "blur(15px)",
    WebkitBackdropFilter: "blur(15px)",
    padding: "50px 40px",
    borderRadius: "30px",
    width: "100%",
    maxWidth: "420px",
    boxShadow: "0 25px 60px rgba(0,0,0,0.15)",
    textAlign: "center",
    transition: "all 0.4s ease",
  },

  iconCircle: {
    width: "80px",
    height: "80px",
    margin: "0 auto 25px auto",
    borderRadius: "50%",
    background: "linear-gradient(135deg, #d35400, #e67e22)",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    fontSize: "2.2rem",
    color: "#fff",
    boxShadow: "0 10px 20px rgba(230, 126, 34, 0.3)",
  },

  title: {
    fontSize: "2rem",
    fontWeight: 700,
    marginBottom: "35px",
    color: "#333",
    letterSpacing: "1px",
  },

  inputGroup: {
    display: "flex",
    flexDirection: "column",
    gap: "18px",
    marginBottom: "30px",
  },

  input: {
    padding: "15px 20px",
    borderRadius: "16px",
    border: "1px solid rgba(0,0,0,0.1)",
    fontSize: "1rem",
    outline: "none",
    transition: "all 0.3s ease",
    background: "#ffffff",
    color: "#333",
  },

  primaryButton: {
    width: "100%",
    padding: "15px",
    borderRadius: "16px",
    border: "none",
    background: "linear-gradient(135deg, #e67e22, #d35400)",
    color: "#fff",
    fontWeight: 700,
    fontSize: "1rem",
    cursor: "pointer",
    boxShadow: "0 10px 20px rgba(230, 126, 34, 0.3)",
    transition: "all 0.3s ease",
  },

  switchText: {
    marginTop: "25px",
    fontSize: "0.9rem",
    color: "#555",
  },

  link: {
    marginLeft: "6px",
    color: "#e67e22",
    fontWeight: 600,
    cursor: "pointer",
  },

  message: {
    marginTop: "20px",
    padding: "12px",
    borderRadius: "12px",
    backgroundColor: "#ffe6e6",
    color: "#d63031",
    fontSize: "0.9rem",
    fontWeight: 600,
  },
  panelContainer: {
    width: "90%",
    maxWidth: "1000px",
    height: "80%",
    backgroundColor: "rgba(255, 255, 255, 0.95)",
    borderRadius: "20px",
    padding: "2rem",
    boxShadow: "0 10px 30px rgba(0,0,0,0.1)",
    backdropFilter: "blur(10px)",
    overflowY: "auto",
  },
  menuWrapper: {
    width: "100vw",
    height: "100vh",
    backgroundColor: "#f4f4f4",
    display: "flex",
    flexDirection: "column"
  },
};
export default App;