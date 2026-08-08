import { Component, inject, OnInit, signal } from '@angular/core';
import { CassaService } from '../../services/cassa.service';
import { CommonModule } from '@angular/common';
import { Ordine, OrdineService } from '../../services/ordine.service';

@Component({
  selector: 'app-cassa-dashboard',
  imports: [CommonModule],
  templateUrl: './cassa-dashboard.component.html',
  styleUrl: './cassa-dashboard.component.css',
})
export class CassaDashboardComponent implements OnInit {
  private ordineService = inject(OrdineService);
  private cassaService = inject(CassaService);

  ordini = signal<Ordine[]>([]);
  reportGiornaliero = signal<Map<string, number>>(new Map());
  loading = signal<boolean>(true);
  ordineSelezionato = signal<Ordine | null>(null);

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading.set(true);

    //Carico gli ordini
    this.ordineService.getAll().subscribe({
      next: (res) => {
        this.ordini.set(res);
        this.loading.set(false);
      }, error: (err) => {
        console.error('Errore nel caricamento ordini:', err);
        this.loading.set(false);
      }
    });

    //Caricamento report giornaliero di cassa
    this.cassaService.getReportGiornaliero().subscribe({
      next: (report) => {
        this.reportGiornaliero.set(new Map(Object.entries(report)));
      }, error: (err) => console.error('Errore nel caricamento report:', err)
    });
  }

  selezionaPerPagamento(ordine: Ordine): void{
    this.ordineSelezionato.set(ordine);
  }

  get ordiniDaSaldare() {
    return this.ordini().filter(o => o.stato === 'SERVITO');
  }

  get totaleIncassatoOggi(): number{
    let totale = 0;
    this.reportGiornaliero().forEach((valore) => totale += valore);
    return totale;
  }
}
