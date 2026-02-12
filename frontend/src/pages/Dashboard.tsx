// src/pages/Dashboard.tsx
import { useEffect, useState } from "react";
import { fetchPizze } from "../api/api";
import type { Pizza } from "../api/api";

export default function Dashboard() {
  const [pizze, setPizze] = useState<Pizza[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchPizze()
      .then(setPizze)
      .catch(err => setError(err.message));
  }, []);

  return (
    <div>
      <h1>Dashboard</h1>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <ul>
        {pizze.map(p => (
          <li key={p.id}>{p.nome} - €{p.prezzo}</li>
        ))}
      </ul>
    </div>
  );
}
