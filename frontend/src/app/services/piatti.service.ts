import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

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

    constructor(private http: HttpClient) { }

    getPiatti(): Observable<Piatto[]> {
        return this.http.get<Piatto[]>(this.API);
    }

    createPaitto(piatto: Piatto): Observable<Piatto> {
        return this.http.post<Piatto>(this.API, piatto);
    }
}