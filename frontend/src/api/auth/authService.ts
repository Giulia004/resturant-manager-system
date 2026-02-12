// src/api/auth/authService.ts

export interface RegisterRequest {
  username: string;
  psw: string;
}

export interface LoginRequest {
  username: string;
  psw: string;
}

export interface AuthResponse {
  token: string | null;
  role: string | null;
  message?: string; //Gestione degli errori per il backend
}

const API_URL = "http://localhost:8080/api/auth";

// ================= REGISTER =================
export const register = async (data: RegisterRequest): Promise<AuthResponse> => {
  try {
    const res = await fetch(`${API_URL}/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });
    const result = await res.json();

    if (!res.ok) throw new Error(result.message || "Errore di registrazione");

    return result;
  } catch (err) {
    console.error("Errore di registrazione: ", err);
    return { token: null, role: null };
  }
};

// ================= LOGIN =================
export const login = async (data: LoginRequest): Promise<AuthResponse> => {
  try {
    const res = await fetch(`${API_URL}/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({username:data.username,password:data.psw}),
    });

    if (!res.ok) {
      return { token: null, role: null };
    }

    const response: AuthResponse = await res.json();

    // Salva il token in localStorage se presente
    if (response.token) {
      localStorage.setItem("token", response.token);
      if (response.role) localStorage.setItem("role", response.role);
    }

    return response;
  }
  catch (err) {
    console.error("Login Error:", err);
    return { token: null, role: null };
  }
};

// ================= LOGOUT =================
export const logout = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("role");
  window.location.href = "/login";//Reindirizzamento
};

// ================= GET TOKEN =================
export const getToken = (): string | null => localStorage.getItem("token");
export const getRole = (): string | null => localStorage.getItem("role");
export const isAuthenticated = (): boolean => !!getToken();
