import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { AuthService } from "./auth.service";
import { Observable } from "rxjs";
import { Ordine } from "./ordine.service";

@Injectable({ providedIn: 'root' })

export class CassaService{
    private http = inject(HttpClient);
    private authService = inject(AuthService);
    private API = "http://localhost:8000/api/cassa";
    private API_ORDINI = "http://localhost:8000/api/ordini";

    //Finalizza il pagamento di un ordine
    finalizzaPagamento(ordineId: number, pagamentoData: { metodoPagamento: string, importoScontato: number }): Observable<Ordine>{
        return this.http.post<Ordine>(`${this.API_ORDINI}/${ordineId}/pagamento`, pagamentoData, { headers: this.authService.getHeaders() });
    }

    getReportStorico(): Observable<Map<String, number>>{
        return this.http.get<Map<String, number>>(`${this.API}/report-storico`, { headers: this.authService.getHeaders() });
    }
}