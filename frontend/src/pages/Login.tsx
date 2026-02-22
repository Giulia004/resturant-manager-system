import React, { useState } from 'react';
import { login } from '../api/api';

interface LoginProps {
    onLoginSuccess: (token: string) => void;
}

export default function LoginView({ onLoginSuccess }: LoginProps) {
    // Cambiamo 'email' in 'username' per coerenza
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleFormSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        try {
            // Assicurati che la funzione login nel file api.ts 
            // accetti questi due parametri
            const data = await login(username, password);

            localStorage.setItem('token', data.token);
            onLoginSuccess(data.token);
        } catch (err: any) {
            setError(err.message || "Credenziali non valide");
        }
    };

    return (
        <div style={styles.container}>
            <div style={styles.card}>
                <div style={styles.iconCircle}>🍕</div>
                <h2 style={styles.title}>Benvenuto</h2>
                <p style={styles.subtitle}>Inserisci le tue credenziali</p>

                <form onSubmit={handleFormSubmit} style={styles.form}>
                    <div style={styles.inputGroup}>
                        <label style={styles.label}>Username</label>
                        <input
                            type="text"
                            value={username}
                            onChange={e => setUsername(e.target.value)}
                            required
                            style={styles.input}
                            placeholder="es. mario.rossi"
                        />
                    </div>
                    <div style={styles.inputGroup}>
                        <label style={styles.label}>Password</label>
                        <input
                            type="password"
                            value={password}
                            onChange={e => setPassword(e.target.value)}
                            required
                            style={styles.input}
                            placeholder="••••••••"
                        />
                    </div>

                    {error && <p style={styles.errorMessage}>{error}</p>}

                    <button type="submit" style={styles.button}>Accedi</button>
                </form>
            </div>
        </div>
    );
}

const styles: { [key: string]: React.CSSProperties } = {
    container: {
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        height: "100vh",
        width: "100%",
        background: "linear-gradient(135deg, #fff8f0 0%, #ffeacc 100%)",
        fontFamily: "'Poppins', sans-serif",
    },
    card: {
        backgroundColor: "#fff",
        padding: "40px 30px",
        borderRadius: "20px",
        width: "90%",
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
    inputGroup: {
        textAlign: "left"
    },
    label: {
        display: "block",
        marginBottom: "8px",
        fontWeight: 600,
        color: "#34495e",
        fontSize: "0.9rem"
    },
    input: {
        width: "100%",
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
    errorMessage: {
        color: "#e74c3c",
        fontSize: "0.9rem",
        margin: "0",
        fontWeight: 500
    }
};