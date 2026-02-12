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
        <div style={{...styles.card, maxWidth: "800px", width: "95%"}}>
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

// ... i tuoi stili rimangono identici ...
const styles: { [key: string]: React.CSSProperties } = {
    // ... (copia i tuoi stili qui)
};

export default App;