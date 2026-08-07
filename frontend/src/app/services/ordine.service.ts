import { inject, Injectable } from "@angular/core";
import { Piatto } from "./piatti.service";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { AuthService } from "./auth.service";
import { Tavolo } from "./tavoli.service";

export type StatoOrdine = 'IN_ATTESA' | 'IN_PREPARAZIONE' | 'PRONTO' | 'SERVITO' | 'PAGATO' | 'ANNULLATO';

export interface OrdineItem {
    id?: number;
    piatto: Piatto;
    qta: number;
    prezzoUnitario: number;
};

export interface Ordine {
    id?: number;
    tavolo: Tavolo;
    dataCreazione: Date;
    stato: StatoOrdine;
    totale: number,
    righe: OrdineItem[]
};

//Riga da inviare
export interface OrdineItemRequest {
    piattoId: number;
    qta: number;
};

//Payload di creazione
export interface OrdineRequest {
    numeroTavolo: number;
    righe: OrdineItemRequest[];
};

@Injectable({
    providedIn: 'root'
})

export class OrdineService {
    private http = inject(HttpClient);
    private authService = inject(AuthService);
    private API = "http://localhost:8000/api/ordini";

    createOrdine(ordine: OrdineRequest): Observable<Ordine> {
        return this.http.post<Ordine>(this.API, ordine, { headers: this.authService.getHeaders() });
    }

    getAll(): Observable<Ordine[]> {
        return this.http.get<Ordine[]>(this.API);
    }

    updateStato(id: number, stato: StatoOrdine): Observable<Ordine> {
        return this.http.put<Ordine>(`${this.API}/${id}/stato`, { stato }, { headers: this.authService.getHeaders() });
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.API}/${id}`, { headers: this.authService.getHeaders() });
    }
}