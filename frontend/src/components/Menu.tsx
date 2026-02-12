import React, { useState, useEffect } from "react";
import { getPizze } from "../api/api";
import type { Pizza } from "../types/Pizza";

interface MenuProps {
  token: string;
  onBack: () => void;
}

const Menu: React.FC<MenuProps> = ({ token, onBack }) => {
  const [pizze, setPizze] = useState<Pizza[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>("");

  useEffect(() => {
    getPizze(token)
      .then((data) => setPizze(data))
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token]);

  return (
    <div style={styles.container}>
      {/* Barra superiore con tasto back e info */}
      <div style={styles.topBar}>
        <button onClick={onBack} style={styles.backButton}>
          <span style={styles.backArrow}>‹</span> Dashboard
        </button>
        <div style={styles.headerInfo}>
          <h1 style={styles.mainTitle}>Menù Esclusivo</h1>
          <p style={styles.pizzaCount}>{pizze.length} Specialità in lista</p>
        </div>
      </div>

      {loading && <div style={styles.loader}>Preparazione menù...</div>}
      {error && <div style={styles.error}>{error}</div>}

      {/* La Griglia Magica */}
      <div style={styles.pizzaGrid}>
        {pizze.map((pizza) => (
          <div key={pizza.id} style={styles.card}>
            {/* Sezione Superiore Card */}
            <div style={styles.cardTop}>
              <div style={styles.emojiContainer}>🍕</div>
              <div style={styles.priceBadge}>{pizza.price.toFixed(2)}€</div>
            </div>

            {/* Sezione Centrale (Contenuto) */}
            <div style={styles.cardContent}>
              <h3 style={styles.pizzaTitle}>{pizza.name}</h3>
              <div style={styles.ingredientsWrapper}>
                {(pizza.ingredients ?? []).map((ing,index) => (
                  <span key={index} style={styles.ingredientTag}>
                    {ing}
                  </span>
                ))}
              </div>
            </div>

            {/* Sezione Inferiore (Stato) */}
            <div style={styles.cardFooter}>
              <div style={{
                ...styles.statusIndicator,
                backgroundColor: pizza.status ? "#27ae60" : "#e74c3c"
              }} />
              <span style={{
                ...styles.statusText,
                color: pizza.status ? "#27ae60" : "#e74c3c"
              }}>
                {pizza.status ? "Disponibile" : "Esaurita"}
              </span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

const styles: { [key: string]: React.CSSProperties } = {
  container: {
    padding: "40px 20px",
    width: "100%",
    maxWidth: "1200px",
    margin: "0 auto",
    minHeight: "100vh",
    fontFamily: "'Poppins', sans-serif",
    backgroundColor: "#fcfcfc",
    color: "#1a1a1a",
  },

  // Top bar
  topBar: {
    display: "flex",
    flexDirection: "column",
    alignItems: "flex-start",
    marginBottom: "50px",
    gap: "20px",
  },
  backButton: {
    background: "#fff",
    border: "none",
    padding: "8px 18px",
    borderRadius: "50px",
    cursor: "pointer",
    fontSize: "0.9rem",
    fontWeight: "600",
    color: "#e67e22",
    display: "flex",
    alignItems: "center",
    boxShadow: "0 2px 10px rgba(0,0,0,0.1)",
    transition: "all 0.3s ease",
  },
  backButtonHover: {
    background: "#fef5f0",
  },
  backArrow: { fontSize: "1.5rem", marginRight: "5px", lineHeight: "0" },
  headerInfo: { textAlign: "left" },
  mainTitle: {
    fontSize: "3rem",
    margin: 0,
    fontWeight: "900",
    color: "#1a1a1a",
    letterSpacing: "-1px",
  },
  pizzaCount: {
    color: "#e67e22",
    fontWeight: "700",
    textTransform: "uppercase",
    fontSize: "0.9rem",
    marginTop: "5px",
  },

  // Griglia responsiva
  pizzaGrid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))",
    gap: "30px",
    width: "100%",
  },

  // Card
  card: {
    backgroundColor: "#fff",
    borderRadius: "24px",
    padding: "24px",
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
    boxShadow: "0 6px 18px rgba(0,0,0,0.06)",
    border: "1px solid #f0f0f0",
    height: "100%",
    transition: "all 0.3s ease",
    cursor: "pointer",
  },
  cardHover: {
    transform: "translateY(-5px)",
    boxShadow: "0 10px 25px rgba(0,0,0,0.1)",
  },
  cardTop: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: "20px",
  },
  emojiContainer: {
    fontSize: "2.5rem",
    background: "#fff3e6",
    width: "60px",
    height: "60px",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    borderRadius: "18px",
    boxShadow: "0 2px 8px rgba(0,0,0,0.05)",
  },
  priceBadge: {
    fontSize: "1.1rem",
    fontWeight: "700",
    color: "#fff",
    background: "#e67e22",
    padding: "6px 14px",
    borderRadius: "12px",
  },
  cardContent: {
    textAlign: "left",
    flexGrow: 1,
  },
  pizzaTitle: {
    fontSize: "1.4rem",
    fontWeight: "700",
    margin: "0 0 15px 0",
    color: "#1a1a1a",
  },
  ingredientsWrapper: {
    display: "flex",
    flexWrap: "wrap",
    gap: "8px",
    marginBottom: "20px",
  },
  ingredientTag: {
    background: "#fff4e6",
    border: "1px solid #ffe6cc",
    padding: "4px 10px",
    borderRadius: "12px",
    fontSize: "0.75rem",
    color: "#e67e22",
    fontWeight: "600",
  },
  cardFooter: {
    display: "flex",
    alignItems: "center",
    paddingTop: "15px",
    borderTop: "1px solid #f5f5f5",
    marginTop: "auto",
  },
  statusIndicator: {
    width: "10px",
    height: "10px",
    borderRadius: "50%",
    marginRight: "8px",
  },
  statusText: {
    fontSize: "0.85rem",
    fontWeight: "700",
    textTransform: "uppercase",
    letterSpacing: "0.5px",
  },

  loader: {
    textAlign: "center",
    padding: "100px",
    fontSize: "1.2rem",
    fontWeight: "bold",
    color: "#e67e22",
  },
  error: {
    color: "#e74c3c",
    textAlign: "center",
    padding: "50px",
    fontWeight: "600",
  },
};
export default Menu;