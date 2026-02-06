
# 🍕 Gestionale Pizzeria — Roadmap Completa (Spring Boot + React)

## 🧠 FASE 0 — PROGETTAZIONE
- Definire ruoli utenti (ADMIN, CAMERIERE, CUCINA, CASSA)
- Disegnare schema database
- Stack:
  - Backend: Spring Boot (REST API)
  - Database: MySQL/PostgreSQL
  - Frontend: React

---

# 🔧 BACKEND — SPRING BOOT (REST)

## 1. Setup Progetto
- Creare progetto Spring Boot
- Dipendenze:
  - Spring Web
  - Spring Data JPA
  - MySQL/Postgres Driver
  - Spring Security
  - Lombok
- Configurare application.properties
- Test connessione DB
- Abilitare CORS per React

## 2. Sistema Utenti e Sicurezza
- Entity Utente
- Enum Ruolo
- Repository Utente
- Service Utente
- Password criptate (BCrypt)
- Login con Spring Security
- Implementare JWT
- Autorizzazioni per ruolo

## 3. Menu (Prodotti)
- Entity Prodotto
- CRUD prodotti
- Campo disponibilità
- Categoria (pizza, bevanda…)
- API REST lista menu

## 4. Tavoli
- Entity Tavolo
- Stato tavolo (LIBERO, OCCUPATO)
- API occupa/libera tavolo
- Lista tavoli API

## 5. Ordini
- Entity Ordine
- Entity RigaOrdine
- Stati ordine:
  - IN_ATTESA
  - IN_PREPARAZIONE
  - PRONTO
  - SERVITO
- Creazione ordine
- Aggiunta prodotti
- Calcolo totale automatico
- Cambio stato ordine API

## 6. Magazzino Ingredienti (Avanzato)
- Entity Ingrediente
- Entity RicettaPizza
- Scarico ingredienti automatico
- Alert scorte basse

## 7. Pagamenti
- Campo pagato in Ordine
- Metodo pagamento
- Chiusura conto
- Storico scontrini

## 8. Funzioni Extra (WOW)
- Report vendite giornaliere
- Pizza più venduta
- Fatturato giorno/settimana

---

# 🎨 FRONTEND — REACT

## 1. Setup React
- Creare progetto con Vite
- Installare:
  - Axios
  - React Router
  - Libreria UI (MUI o Ant Design)
- Configurare struttura cartelle
- Creare servizio API base

## 2. Autenticazione
- Pagina Login
- Invio credenziali a Spring Boot
- Salvataggio JWT
- Protezione rotte
- Logout

## 3. Layout
- Navbar
- Sidebar
- Mostrare utente loggato
- Sistema routing

## 4. Menu
- Pagina gestione menu (admin)
- Form aggiunta prodotto
- Lista prodotti (API)

## 5. Tavoli
- Schermata tavoli a griglia
- Colore tavolo libero/occupato
- Click tavolo → apri ordine

## 6. Ordini
- Schermata creazione ordine
- Aggiunta prodotti
- Mostra totale
- Cambio stato ordine

## 7. Cucina
- Pagina ordini in preparazione
- Bottone “PRONTO”

## 8. Cassa
- Pagina ordini da pagare
- Chiusura conto

## 9. Magazzino
- Lista ingredienti
- Quantità + soglia minima
- Evidenzia scorte basse

---

# 🧩 FASE FINALE
- Gestione errori frontend/backend
- Validazioni form
- Messaggi successo/errore
- Test completi
- README GitHub
- Screenshot progetto
- Deploy (facoltativo)

---

# 🏁 LIVELLI COMPLETAMENTO

**Base:** Login + Menu + Tavoli + Ordini  
**Intermedio:** Ruoli + Cucina + Cassa  
**Avanzato:** Magazzino automatico + Report + JWT
