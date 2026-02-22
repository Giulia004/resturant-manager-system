// src/pages/Dashboard.tsx
import React from "react";

interface DashboardProps {
  onLogout: () => void;
  onMenuClick: () => void;
  onControlPanelClick: () => void;
}

const Dashboard: React.FC<DashboardProps> = ({ onLogout, onMenuClick, onControlPanelClick }) => {
  // Recupero dati dal localStorage
  const role = localStorage.getItem("role") || "GUEST";

  const tableNumber = localStorage.getItem("tableNumber");

  // Logica di riconoscimento: è un utente tavolo se ha il ruolo USER e un numero tavolo assegnato
  const isTableUser = role === 'USER' && tableNumber !== null;

  // Determina il testo del badge
  const displayInfo = isTableUser
    ? `Tavolo ${tableNumber}`
    : role;

  return (
    <div style={styles.container}>
      <div style={styles.dashboard}>
        <header style={styles.header}>
          <div>
            <h1 style={styles.welcome}>Dashboard 🍕</h1>
            <span style={styles.badge}>{displayInfo.toUpperCase()}</span>
          </div>
          <button onClick={onLogout} style={styles.logoutBtn}>Esci</button>
        </header>

        <div style={styles.grid}>

          {/* 1. SEZIONE ORDINI: Dinamica in base a chi logga */}
          {role != "USER" && (
            <div style={styles.card} className="hover-card">
              <div style={styles.iconBox}>📋</div>
              <div>
                <h3 style={styles.cardTitle}>
                  {isTableUser ? "Il mio Ordine" : "Gestione Comande"}
                </h3>
                <p style={styles.cardText}>
                  {isTableUser ? "Controlla lo stato dei tuoi piatti" : "Visualizza e gestisci le comande in cucina"}
                </p>
              </div>
            </div>
          )}

          {/* 2. SEZIONE MENU: Accessibile a tutti */}
          <div style={styles.cardPizza} onClick={onMenuClick} className="hover-card">
            <div style={{ ...styles.iconBox, backgroundColor: 'rgba(255,255,255,0.2)' }}>🍕</div>
            <div>
              <h3 style={styles.cardTitle}>Menù</h3>
              <p style={{ ...styles.cardText, color: '#fff' }}>
                {isTableUser ? "Ordina ora" : "Consulta i prodotti"}
              </p>
            </div>
          </div>

          {/* 3. SEZIONE TAVOLI: Nascosta per i clienti (isTableUser) */}
          {role != "USER" && !isTableUser && (
            <div style={styles.card} className="hover-card">
              <div style={styles.iconBox}>🪑</div>
              <div>
                <h3 style={styles.cardTitle}>Mappa Sala</h3>
                <p style={styles.cardText}>Stato occupazione tavoli</p>
              </div>
            </div>
          )}

          {/* 4. ADMIN PANEL: Solo per Admin */}
          {role === "ADMIN" && (
            <div style={{ ...styles.card, border: "2px solid #e67e22" }} className="hover-card" onClick={onControlPanelClick}>
              <div style={styles.iconBox}>⚙️</div>
              <div>
                <h3 style={styles.cardTitle}>Pannello Controllo</h3>
                <p style={styles.cardText}>Configurazione sistema e log</p>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

// ... stili costanti (identici ai precedenti)
const styles: { [key: string]: React.CSSProperties } = {
  container: { width: "100vw", height: "100vh", display: "flex", justifyContent: "center", alignItems: "center", background: "linear-gradient(135deg, #e67e22 0%, #d35400 100%)", position: "fixed", top: 0, left: 0 },
  dashboard: { width: "90%", maxWidth: "1000px", height: "80%", backgroundColor: "rgba(255, 255, 255, 0.95)", borderRadius: "20px", padding: "2rem", boxShadow: "0 10px 30px rgba(0,0,0,0.1)", backdropFilter: "blur(10px)", overflowY: "auto" },
  header: { display: "flex", justifyContent: "space-between", alignItems: "flex-start", borderBottom: "1px solid #eee", paddingBottom: "1.5rem", marginBottom: "2rem" },
  welcome: { margin: 0, color: "#2c3e50", fontSize: "1.8rem" },
  badge: { backgroundColor: "#e67e22", color: "white", padding: "5px 15px", borderRadius: "15px", fontSize: "0.8rem", fontWeight: "bold", display: "inline-block", marginTop: "5px" },
  grid: { display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(200px, 1fr))", gap: "1.5rem" },
  card: { padding: "1.5rem", borderRadius: "12px", backgroundColor: "#fff", color: "#2c3e50", border: "none", textAlign: "left", cursor: "pointer", boxShadow: "0 4px 15px rgba(0,0,0,0.05)", display: "flex", alignItems: "center", gap: "15px", transition: "all 0.3s" },
  cardPizza: { padding: "1.5rem", borderRadius: "12px", backgroundColor: "#fe6100", color: "#fff", textAlign: "left", cursor: "pointer", boxShadow: "0 8px 20px rgba(254, 97, 0, 0.3)", display: "flex", alignItems: "center", gap: "15px", border: "none", transition: "all 0.3s" },
  iconBox: { width: "50px", height: "50px", borderRadius: "12px", backgroundColor: "#f8f9fa", display: "flex", alignItems: "center", justifyContent: "center", fontSize: "1.5rem" },
  cardTitle: { margin: "0 0 5px 0", fontSize: "1.1rem", fontWeight: "bold" },
  cardText: { margin: 0, fontSize: "0.9rem", color: "#7f8c8d" },
  logoutBtn: { padding: "8px 20px", borderRadius: "8px", border: "none", backgroundColor: "#e74c3c", color: "white", cursor: "pointer", fontWeight: "bold" }
};

export default Dashboard;