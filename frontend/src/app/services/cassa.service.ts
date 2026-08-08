import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { AuthService } from "./auth.service";
import { Observable } from "rxjs";
import { Ordine } from "./ordine.service";

export interface PagamentoRequest {
    metodoPagamento: string;
    sconto?: number;
};

@Injectable({ providedIn: 'root' })

export class CassaService{
    private http = inject(HttpClient);
    private authService = inject(AuthService);
    private API = "http://localhost:8000/api/cassa";
    private API_ORDINI = "http://localhost:8000/api/ordini";

    getReportGiornaliero():Observable<Map<String,number>> {
        return this.http.get<Map<String, number>>(this.API + "/report-giornaliero", { headers: this.authService.getHeaders() });
    }

    //Finalizza il pagamento di un ordine
    finalizzaPagamento(ordineId: number, request: PagamentoRequest): Observable<Ordine>{
        return this.http.put<Ordine>(`${this.API_ORDINI}/${ordineId}/pagamento`, request, { headers: this.authService.getHeaders() });
    }
}