export interface Ingredients{
    id: number;
    name: string;
}
export interface Pizza{
    id: number;
    name: string;
    price: number;
    status: boolean;
    categoria: string;
    ingredients: string[];
}