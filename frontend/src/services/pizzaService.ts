import axios from "axios";
import type { Pizza } from "../types/Pizza";

const API_URL = "http://localhost:8080/api/pizze";

// Helper per creare gli header in modo pulito
const getAuthHeaders = (token?: string) => ({
    headers: token ? { Authorization: `Bearer ${token}` } : {},
});

export const getPizza = async (token?: string): Promise<Pizza[]> => {
    const response = await axios.get<Pizza[]>(API_URL, getAuthHeaders(token));
    return response.data;
};

export const createPizza = async (pizza: Omit<Pizza, "id">, token?: string): Promise<Pizza> => {
    const response = await axios.post<Pizza>(API_URL, pizza, getAuthHeaders(token));
    return response.data;
};

export const updatePizza = async (
    id: number, 
    pizza: Omit<Pizza, "id">, 
    token?: string
): Promise<Pizza> => {
    const response = await axios.put<Pizza>(`${API_URL}/${id}`, pizza, getAuthHeaders(token));
    return response.data;
};

export const deletePizza = async (id: number, token?: string): Promise<void> => {
    await axios.delete(`${API_URL}/${id}`, getAuthHeaders(token));
};