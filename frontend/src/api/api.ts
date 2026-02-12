import type { Pizza } from "../types/Pizza";

interface LoginResponse {
    token: string;
    username: string;
    id: number
    role: string;
}
// src/api/api.ts
const BASE_URL = "http://localhost:8080/api";

export const login = async (username: string, psw: string): Promise<LoginResponse> => {
    const res = await fetch(`${BASE_URL}/auth/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, psw })
    });

    if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        throw new Error(errorData.message || `Errore login: ${res.status}`);
    }
    return res.json();
};

export const getPizze = async (token: string): Promise<Pizza[]> => {
    try {
        const response = await fetch(`${BASE_URL}/pizze`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
        });

        if (!response.ok) {
            if (response.status == 401) throw new Error("Sessione scaduta");
            throw new Error(`Errore server: ${response.status}`);
        }
        const data: Pizza[] = await response.json();
        return data;
    } catch (error) {
        console.error("Errore nel recupero pizze:", error);
        throw error;
    }
};