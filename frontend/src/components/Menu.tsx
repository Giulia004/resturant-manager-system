import React, { useState, useEffect } from "react";
import axios from "axios";

// 1. Definiamo le Props che App.tsx ci invia
interface MenuProps {
  token: string;
  tableNumber?: number;
  onBack: () => void;
}

// 2. Interfacce per i dati del backend
interface Prodotto {
  id: number;
  nome: string;
  descrizione: string;
  prezzo: number;
  categoria: "PRIMI" | "SECONDI" | "BEVANDA" | "DESSERT" | "PIZZA";
}

// Interfaccia per gli elementi del carrello (estende Prodotto con la quantità)
interface CartItem extends Prodotto {
  qta: number;
}

const Menu: React.FC<MenuProps> = ({ token, tableNumber, onBack }) => {
  const [prodotti, setProdotti] = useState<Prodotto[]>([]);
  const [categoriaAttiva, setCategoriaAttiva] = useState<string>("PIZZA");
  const [carrello, setCarrello] = useState<CartItem[]>([]);
  

  // Caricamento prodotti dal backend
  useEffect(() => {
    // Aggiungiamo il token negli header per la sicurezza
    const config = {
      headers: { Authorization: `Bearer ${token}` }
    };

    axios.get(`http://localhost:8080/api/prodotti/categoria/${categoriaAttiva}`, config)
      .then(res => setProdotti(res.data))
      .catch(err => console.error("Errore nel caricamento:", err));
  }, [categoriaAttiva, token]);

  // Gestione aggiunta al carrello
  const addToCart = (prodotto: Prodotto) => {
    setCarrello((prev) => {
      const existing = prev.find((item) => item.id === prodotto.id);
      if (existing) {
        return prev.map((item) =>
          item.id === prodotto.id ? { ...item, qta: item.qta + 1 } : item
        );
      }
      return [...prev, { ...prodotto, qta: 1 }];
    });
  };

  // Funzione per inviare l'ordine al backend
  const handleCheckout = async () => {
    if (carrello.length === 0) return;

    // CONFIGURAZIONE TABLET FISSO
    // In produzione, questi dati verrebbero dal login del tablet
    const tableId = tableNumber || 1; // Usa il numero tavolo passato o 1 come fallback
    const fixedUserId = 4;  // ID dell'utente 'tavolo1' (creato in DemoApplication)

    const orderRequest = {
      tableId: tableId,
      cameriereId: fixedUserId,
      items: carrello.map(item => ({
        productId: item.id,
        qta: item.qta,
        note: ""
      }))
    };

    try {
      await axios.post("http://localhost:8080/api/ordini", orderRequest, {
        headers: { Authorization: `Bearer ${token}` }
      });
      alert("Ordine inviato in cucina! 👨‍🍳");
      setCarrello([]); // Svuota il carrello dopo l'invio
    } catch (error) {
      console.error("Errore invio ordine:", error);
      alert("Errore durante l'invio dell'ordine. Riprova.");
    }
  };

  return (
    <div style={styles.menuContainer}>
      <header style={styles.menuHeader}>
        <h2 style={styles.headerTitle}>Menù <span style={{ color: '#e67e22' }}>{categoriaAttiva}</span></h2>
        <button onClick={onBack} style={styles.backBtn}>← Home</button>
      </header>

      <div style={styles.layout}>
        {/* Sidebar Categorie */}
        <nav style={styles.sidebar}>
          {["PIZZA", "PRIMI", "SECONDI", "BEVANDA", "DESSERT"].map((cat) => (
            <button
              key={cat}
              onClick={() => setCategoriaAttiva(cat)}
              style={{
                ...styles.catBtn,
                backgroundColor: categoriaAttiva === cat ? "#e67e22" : "transparent",
                color: categoriaAttiva === cat ? "#fff" : "#555",
                boxShadow: categoriaAttiva === cat ? "0 4px 12px rgba(230, 126, 34, 0.3)" : "none",
              }}
            >
              {cat}
            </button>
          ))}
        </nav>

        {/* Griglia Prodotti */}
        <main style={styles.grid}>
          {prodotti.map((p) => (
            <div key={p.id} style={styles.productCard}>
              <div>
                <h4 style={styles.productName}>{p.nome}</h4>
                <p style={styles.productDesc}>{p.descrizione}</p>
              </div>
              <div style={styles.cardFooter}>
                <span style={styles.priceTag}>{p.prezzo.toFixed(2)}€</span>
                <button style={styles.addBtn} onClick={() => addToCart(p)}>+</button>
              </div>
            </div>
          ))}
        </main>

        {/* Sidebar Carrello */}
        <aside style={styles.cartSidebar}>
          <h3 style={styles.cartTitle}>Il tuo Ordine</h3>
          <div style={styles.cartItems}>
            {carrello.length === 0 ? (
              <p style={{ color: '#bdc3c7', textAlign: 'center', marginTop: '20px' }}>Il carrello è vuoto</p>
            ) : (
              carrello.map((item) => (
                <div key={item.id} style={styles.cartItem}>
                  <div style={{ display: 'flex', flexDirection: 'column' }}>
                    <span style={{ fontWeight: 600 }}>{item.nome}</span>
                    <span style={{ fontSize: '0.8rem', color: '#95a5a6' }}>x{item.qta}</span>
                  </div>
                  <span style={{ fontWeight: 'bold' }}>{(item.prezzo * item.qta).toFixed(2)}€</span>
                </div>
              ))
            )}
          </div>
          <div style={styles.cartTotal}>
            Totale: {carrello.reduce((acc, item) => acc + item.prezzo * item.qta, 0).toFixed(2)}€
          </div>
          <button
            style={styles.checkoutBtn}
            disabled={carrello.length === 0}
            onClick={handleCheckout}
          >
            Conferma Ordine
          </button>
        </aside>
      </div>
    </div>
  );
};

// Stili base veloci
const styles: { [key: string]: React.CSSProperties } = {
  menuContainer: {
    display: "flex",
    flexDirection: "column",
    height: "100vh",
    width: "100%",
    backgroundColor: "#f8f9fa",
    fontFamily: "'Poppins', sans-serif",
    overflow: "hidden"
  },
  menuHeader: {
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    padding: "1rem 2rem",
    backgroundColor: "#fff",
    boxShadow: "0 2px 10px rgba(0,0,0,0.05)",
    zIndex: 10
  },
  headerTitle: {
    margin: 0,
    color: "#2c3e50",
    fontSize: "1.5rem"
  },
  backBtn: {
    padding: "8px 16px",
    borderRadius: "8px",
    border: "none",
    cursor: "pointer",
    backgroundColor: "transparent",
    color: "#e67e22",
    fontWeight: "600",
    transition: "all 0.2s"
  },
  layout: {
    display: "flex",
    flex: 1,
    overflow: "hidden",
    padding: "20px",
    gap: "20px"
  },
  sidebar: {
    width: "180px",
    display: "flex",
    flexDirection: "column",
    gap: "10px",
    overflowY: "auto",
    paddingRight: "5px"
  },
  catBtn: {
    padding: "12px 15px",
    borderRadius: "12px",
    border: "none",
    cursor: "pointer",
    fontWeight: "600",
    textAlign: "left",
    fontSize: "0.95rem",
    transition: "all 0.2s ease"
  },
  grid: {
    flex: 1,
    display: "grid",
    gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))",
    gap: "20px",
    overflowY: "auto",
    paddingBottom: "20px",
    alignContent: "start"
  },
  productCard: {
    padding: "20px",
    borderRadius: "16px",
    backgroundColor: "#fff",
    border: "none",
    textAlign: "left",
    boxShadow: "0 4px 15px rgba(0,0,0,0.05)",
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
    transition: "transform 0.2s",
    height: "100%"
  },
  productName: { margin: "0 0 5px 0", color: "#2c3e50", fontSize: "1.1rem" },
  productDesc: { fontSize: '0.85rem', color: '#7f8c8d', marginBottom: "15px", flex: 1 },
  cardFooter: { display: "flex", justifyContent: "space-between", alignItems: "center", marginTop: "10px" },
  priceTag: { fontWeight: "bold", color: "#2c3e50", fontSize: "1.1rem" },
  addBtn: { padding: "8px 16px", borderRadius: "20px", border: "none", backgroundColor: "#e67e22", color: "#fff", cursor: "pointer", fontWeight: "bold", boxShadow: "0 4px 10px rgba(230, 126, 34, 0.3)" },
  cartSidebar: { width: "320px", padding: "25px", backgroundColor: "#fff", borderRadius: "20px", display: "flex", flexDirection: "column", boxShadow: "0 10px 30px rgba(0,0,0,0.05)", height: "fit-content", maxHeight: "100%" },
  cartTitle: { margin: "0 0 20px 0", color: "#2c3e50", borderBottom: "2px solid #f0f0f0", paddingBottom: "15px" },
  cartItems: { flex: 1, overflowY: "auto", marginBottom: "20px", maxHeight: "60vh" },
  cartItem: { display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "12px", fontSize: "0.95rem", color: "#34495e" },
  cartTotal: { fontWeight: "bold", fontSize: "1.3rem", margin: "0 0 20px 0", textAlign: "right", color: "#2c3e50" },
  checkoutBtn: { width: "100%", padding: "15px", backgroundColor: "#27ae60", color: "#fff", border: "none", borderRadius: "12px", cursor: "pointer", fontWeight: "bold", fontSize: "1rem", boxShadow: "0 4px 15px rgba(39, 174, 96, 0.3)", transition: "transform 0.1s" }
};

export default Menu;