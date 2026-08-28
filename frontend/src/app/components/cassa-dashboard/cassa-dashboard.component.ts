import { Component, inject, OnInit, PLATFORM_ID, signal, AfterViewInit } from '@angular/core';
import { CassaService } from '../../services/cassa.service';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Ordine, OrdineService, StatoOrdine } from '../../services/ordine.service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { FormsModule } from '@angular/forms';
import Chart from 'chart.js/auto';

@Component({
  standalone: true,
  selector: 'app-cassa-dashboard',
  imports: [CommonModule, FormsModule, MatSnackBarModule],
  templateUrl: './cassa-dashboard.component.html',
  styleUrl: './cassa-dashboard.component.css',
})
export class CassaDashboardComponent implements OnInit, AfterViewInit {
  private ordineService = inject(OrdineService);
  private cassaService = inject(CassaService);
  private snackBar = inject(MatSnackBar);
  private platformId = inject(PLATFORM_ID);

  ordini = signal<Ordine[]>([]);

  reportTotale = signal<Map<string, number>>(new Map());
  ordiniPagatiStorico = signal<Ordine[]>([]);

  loading = signal<boolean>(true);
  ordineSelezionato = signal<Ordine | null>(null);

  // Campi per la gestione del pagamento
  scontoApplicato = signal<number>(0);
  tipoStatoSconto: 'EUR' | 'PERC' = 'EUR';
  metodoPagamento = signal<'CONTANTI' | 'POS'>('CONTANTI');
  importoRicevuto = signal<number | null>(null);

  // Filtri
  filtroPeriodo = signal<'OGGI' | 'TUTTI'>('OGGI');

  private chartInstance: any = null;
  private modalInstance: any = null;

  ngOnInit(): void {
    this.loadData();
  }

  async ngAfterViewInit(): Promise<void> {
    if (isPlatformBrowser(this.platformId)) {
      const bootstrap = await import('bootstrap');
      const modalElement = document.getElementById('pagamentoModal');
      if (modalElement) {
        this.modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
      }
      setTimeout(() => this.initGrafico(), 50);
    }
  }

  loadData(): void {
    this.loading.set(true);

    this.ordineService.getAll().subscribe({
      next: (res) => {
        this.ordini.set(res);
        this.loading.set(false);
      }, error: (err) => {
        console.error('Errore nel caricamento ordini:', err);
        this.loading.set(false);
      }
    });

    this.cassaService.getReportStorico().subscribe({
      next: (reportData: any) => {
        if (reportData && !Array.isArray(reportData)) {
          this.reportTotale.set(new Map(Object.entries(reportData)));
        }
        // Se invece l'endpoint restituisce la lista degli ordini pagati, puoi mapparli qui
        else if (Array.isArray(reportData)) {
          this.ordiniPagatiStorico.set(reportData);
        }

        if (isPlatformBrowser(this.platformId)) {
          setTimeout(() => this.initGrafico(), 50);
        }
      },
      error: (err) => console.error("Errore nel caricamento dei dati dal server", err)
    });
  }

  initGrafico(): void {
    const canvas = document.getElementById('metodiPagamentoChart') as HTMLCanvasElement;
    if (!canvas) return;

    const report = this.reportAttivo;
    const labels = Array.from(report.keys());
    const data = Array.from(report.values());

    if (this.chartInstance) {
      this.chartInstance.destroy();
    }

    this.chartInstance = new Chart(canvas, {
      type: 'doughnut',
      data: {
        labels: labels.length > 0 ? labels : ['Nessun incasso'],
        datasets: [{
          data: data.length > 0 ? data : [1],
          backgroundColor: ['#f59e0b', '#10b981', '#3b82f6', '#64748b'],
          borderWidth: 2,
          borderColor: '#ffffff'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom'
          }
        }
      }
    });
  }

  async selezionaPerPagamento(ordine: Ordine): Promise<void> {
    this.ordineSelezionato.set(ordine);
    this.scontoApplicato.set(0);
    this.tipoStatoSconto = 'EUR';
    this.metodoPagamento.set('CONTANTI');
    this.importoRicevuto.set(ordine.totale);

    if (isPlatformBrowser(this.platformId)) {
      const bootstrap = await import('bootstrap');
      const modalElement = document.getElementById('pagamentoModal');
      if (modalElement) {
        this.modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
        this.modalInstance.show();
      }
    }
  }

  get scontoEffettivoInEuro(): number {
    const ordine = this.ordineSelezionato();
    if (!ordine) return 0;
    const subtotale = ordine.totale || 0;
    const valore = this.scontoApplicato() || 0;

    if (this.tipoStatoSconto === 'PERC') {
      return (subtotale * valore) / 100;
    }
    return valore;
  }

  get totaleFinale(): number {
    const ordine = this.ordineSelezionato();
    if (!ordine) return 0;
    const base = ordine.totale || 0;
    return Math.max(0, base - this.scontoEffettivoInEuro);
  }

  get resto(): number {
    const ricevuto = this.importoRicevuto() || 0;
    return Math.max(0, ricevuto - this.totaleFinale);
  }

  impostaTaglio(valore: number): void {
    this.importoRicevuto.set(Number(this.totaleFinale.toFixed(2)));
  }

  confermaPagamento(): void {
    const ordine = this.ordineSelezionato();
    if (!ordine || !ordine.id) return;

    const payload = {
      metodoPagamento: this.metodoPagamento(),
      importoScontato: this.scontoEffettivoInEuro
    };

    this.cassaService.finalizzaPagamento(ordine.id, payload).subscribe({
      next: () => {
        this.snackBar.open('Pagamento registrato con successo!', 'Chiudi', { duration: 3000 });
        this.chiudiModale();
        this.loadData();
      },
      error: (err) => {
        console.error('Errore durante il pagamento:', err);
        const msg = err.error?.message || 'Errore durante la registrazione del pagamento.';
        this.snackBar.open(msg, 'Chiudi', { duration: 4000 });
      }
    });
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

  get reportGiornaliero(): Map<string, number> {
    const map = new Map<string, number>();
    const oggiStr = new Date().toISOString().split('T')[0];

    const ordiniPagati = this.ordiniPagatiStorico();
    const ordiniOggi = ordiniPagati.filter(o => o.dataCreazione && o.dataCreazione.startsWith(oggiStr));

    ordiniOggi.forEach(ordine => {
      const metodo = ordine.metodoPagamento || 'N/D';
      const corrente = map.get(metodo) || 0;
      map.set(metodo, corrente + (ordine.totale || 0));
    });

    return map;
  }

  get reportAttivo(): Map<string, number> {
    return this.filtroPeriodo() === 'OGGI' ? this.reportGiornaliero : this.reportTotale();
  }

  get totaleIncassatoDisplay(): number {
    let totale = 0;
    this.reportAttivo.forEach((valore) => totale += valore);
    return totale;
  }

  get totaleScontiOggi(): number {
    const oggiStr = new Date().toISOString().split('T')[0];
    return this.ordiniPagatiStorico()
      .filter(o => o.stato === 'PAGATO' && o.dataCreazione && o.dataCreazione.startsWith(oggiStr))
      .reduce((acc, o) => acc + (o.sconto || 0), 0);
  }

  cambiaFiltro(periodo: 'OGGI' | 'TUTTI'): void {
    this.filtroPeriodo.set(periodo);
    if (isPlatformBrowser(this.platformId)) {
      setTimeout(() => this.initGrafico(), 50);
    }
  }
}