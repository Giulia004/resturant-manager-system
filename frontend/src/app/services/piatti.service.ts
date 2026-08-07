import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { AuthService } from "./auth.service";

export interface Piatto {
    id?: number;
    nome: string;
    prezzo: number;
    categoria: string;
};

@Injectable({
    providedIn: 'root'
})

export class PiattiService {
    private API = 'http://localhost:8000/api/piatti';
    private authService = inject(AuthService);

    constructor(private http: HttpClient) { }

    getPiatti(): Observable<Piatto[]> {
        return this.http.get<Piatto[]>(this.API, { headers: this.authService.getHeaders() });
    }

    getPiattoById(id: number): Observable<Piatto> {
        return this.http.get<Piatto>(`${this.API}/${id}`, { headers: this.authService.getHeaders() });
    }

    createPiatto(piatto: Piatto): Observable<Piatto> {
        return this.http.post<Piatto>(this.API, piatto, { headers: this.authService.getHeaders() });
    }

    updatePiatto(id: number, piatto: Partial<Piatto>): Observable<Piatto> {
        return this.http.put<Piatto>(`${this.API}/${id}`, piatto, { headers: this.authService.getHeaders() });
    }

    deletePiatto(id: number): Observable<any>{
        return this.http.delete(`${this.API}/${id}`, { headers: this.authService.getHeaders() });
    }
}