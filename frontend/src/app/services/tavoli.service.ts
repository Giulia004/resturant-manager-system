import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

export interface Tavolo{
    id?: number;
    numero: number;
    posti: number;
    disponibile:true
};

@Injectable({
    providedIn: 'root'
})

export class TavoliService{
    private API = 'http://localhost:8000/api/tavoli';

    constructor(private http: HttpClient) { }

    getTavoli(): Observable<Tavolo[]>{
        return this.http.get<Tavolo[]>(this.API);
    }

    createTavolo(tavolo:Tavolo): Observable<Tavolo>{
        return this.http.post<Tavolo>(this.API, tavolo);
    }
}