import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { AuthService } from "./auth.service";

export interface Tavolo {
    id?: number;
    numero: number;
    posti: number;
    disponibile: boolean
};

@Injectable({
    providedIn: 'root'
})

export class TavoliService {
    private API = 'http://localhost:8000/api/tavoli';
    private authService = inject(AuthService);

    constructor(private http: HttpClient) { }

    getTavoli(): Observable<Tavolo[]> {
        return this.http.get<Tavolo[]>(this.API,{headers:this.authService.getHeaders()});
    }

    getById(id: number): Observable<Tavolo> {
        return this.http.get<Tavolo>(`${this.API}/${id}`,{headers:this.authService.getHeaders()});
    }

    createTavolo(tavolo: Tavolo): Observable<Tavolo> {
        return this.http.post<Tavolo>(this.API, tavolo,{headers:this.authService.getHeaders()});
    }

    updateTavolo(id: number, tavolo: Partial<Tavolo>): Observable<Tavolo>{
        return this.http.put<Tavolo>(`${this.API}/${id}`, tavolo,{headers:this.authService.getHeaders()});
    }

    deleteTavolo(id: number): Observable<void>{
        return this.http.delete<void>(`${this.API}/${id}`,{headers:this.authService.getHeaders()});
    }
}