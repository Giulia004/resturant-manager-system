import React, { useState } from 'react';
import { login } from '../api/api';

interface LoginProps {
  onLoginSuccess: (token: string) => void;
}

export default function LoginView({ onLoginSuccess }: LoginProps) {
    // Cambiamo 'email' in 'username' per coerenza
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleFormSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        
        try {
            // Assicurati che la funzione login nel file api.ts 
            // accetti questi due parametri
            const data = await login(username, password);
            
            localStorage.setItem('token', data.token);
            onLoginSuccess(data.token);
            
            alert("Login effettuato!");
        } catch (err: any) {
            alert("Errore: " + err.message);
        }
    };

    return (
        <div className="login-box">
            <form onSubmit={handleFormSubmit}>
                <div>
                    <label>Username:</label>
                    <input 
                        type="text" 
                        value={username} 
                        onChange={e => setUsername(e.target.value)} 
                        required 
                    />
                </div>
                <div>
                    <label>Password:</label>
                    <input 
                        type="password" 
                        value={password} 
                        onChange={e => setPassword(e.target.value)} 
                        required 
                    />
                </div>
                <button type="submit">Accedi</button>
            </form>
        </div>
    );
}