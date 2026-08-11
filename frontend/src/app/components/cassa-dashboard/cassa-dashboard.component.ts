import { Component, inject, OnInit, PLATFORM_ID, signal } from '@angular/core';
import { CassaService } from '../../services/cassa.service';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Ordine, OrdineService, StatoOrdine } from '../../services/ordine.service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-cassa-dashboard',
  imports: [CommonModule, FormsModule, MatSnackBarModule],
  templateUrl: './cassa-dashboard.component.html',
  styleUrl: './cassa-dashboard.component.css',
})
export class CassaDashboardComponent implements OnInit {
  private ordineService = inject(OrdineService);
  private cassaService = inject(CassaService);
  private snackBar = inject(MatSnackBar);
  private platformId = inject(PLATFORM_ID);

  ordini = signal<Ordine[]>([]);
  reportGiornaliero = signal<Map<string, number>>(new Map());
  loading = signal<boolean>(true);
  ordineSelezionato = signal<Ordine | null>(null);

  //Campi per la gestione del pagamento
  scontoApplicato = signal<number>(0);
  metodoPagamento = signal<string>('CONTANTI');
  importoRicevuto = signal<number | null>(null);

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

  async selezionaPerPagamento(ordine: Ordine): Promise<void> {
    this.ordineSelezionato.set(ordine);
    this.scontoApplicato.set(ordine.sconto || 0);
    this.importoRicevuto.set(ordine.totale);
    this.metodoPagamento.set('CONTANTI');

    if (isPlatformBrowser(this.platformId)) {
      const bootstrap = await import('bootstrap');
      const modalElement = document.getElementById('pagamentoModal');
      if (modalElement) {
        const modal = new bootstrap.Modal(modalElement);
        modal.show();
      }
    }
  }

  get totaleFinale(): number {
    const ordine = this.ordineSelezionato();
    if (!ordine) return 0;
    const base = ordine.totale || 0;
    const sconto = this.scontoApplicato() || 0;
    return Math.max(0, base - sconto);
  }

  get resto(): number {
    const ricevuto = this.importoRicevuto() || 0;
    return Math.max(0, ricevuto - this.totaleFinale);
  }

  confermaPagamento(): void {
    const ordine = this.ordineSelezionato();
    if (!ordine || !ordine.id) return;

    const payload = {
      metodoPagamento: this.metodoPagamento(),
      importoScontato: this.scontoApplicato()
    };

    this.cassaService.finalizzaPagamento(ordine.id, payload).subscribe({
      next: () => {
        this.snackBar.open('Pagamento registrato con successo!', 'Chiudi', { duration: 3000 });
        this.chiudiModale();
        this.loadData(); // Ricarica dati e report
      },
      error: (err) => {
        console.error('Errore durante il pagamento:', err);
        const msg = err.error?.message || 'Errore durante la registrazione del pagamento.';
        this.snackBar.open(msg, 'Chiudi', { duration: 4000 });
      }
    })
  }

  async chiudiModale(): Promise<void> {
    if (isPlatformBrowser(this.platformId)) {
      const bootstrap = await import('bootstrap');
      const modalElement = document.getElementById('pagamentoModal');
      if (modalElement) {
        const modal = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
        modal.hide();
      }
    }
  }

  get ordiniDaSaldare() {
    return this.ordini().filter(o => o.stato === 'SERVITO');
  }

  get totaleIncassatoOggi(): number {
    let totale = 0;
    this.reportGiornaliero().forEach((valore) => totale += valore);
    return totale;
  }
}
