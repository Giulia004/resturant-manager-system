import { CommonModule, isPlatformBrowser } from '@angular/common';
import { AfterViewInit, Component, inject, OnInit, PLATFORM_ID, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Chart } from 'chart.js';

export interface DocumentoFiscale {
  id: number;
  numeroDocumento: string;
  dataEmissione: string;
  intestatario: string;
  partitaIvaCF: string;
  tipo: 'FATTURA' | 'RICEVUTA';
  totale: number;
};

@Component({
  selector: 'app-contabilita-dashboard',
  imports: [CommonModule, FormsModule, MatSnackBarModule],
  templateUrl: './contabilita-dashboard.component.html',
  styleUrl: './contabilita-dashboard.component.css',
})

export class ContabilitaDashboardComponent implements OnInit, AfterViewInit {
  private platformId = inject(PLATFORM_ID);
  private snackBar = inject(MatSnackBar);

  loading = signal<boolean>(false);
  periodoSelezionato = signal<'OGGI' | 'SETTIMANA' | 'MESE'>('OGGI');

  //KPI Finanziari
  fatturatoNetto = signal<number>(0.0);
  numeroFattureEmesse = signal<number>(8);
  totaleCorrispettivi = signal<number>(980.00);

  //Elenco documenti fiscali
  documenti = signal<DocumentoFiscale[]>([
    { id: 1, numeroDocumento: 'FAT-2026/042', dataEmissione: '2026-08-12T14:30:00', intestatario: 'Mario Rossi', partitaIvaCF: 'RSSMRA80A01H501W', tipo: 'FATTURA', totale: 145.00 },
    { id: 2, numeroDocumento: 'FAT-2026/043', dataEmissione: '2026-08-12T15:10:00', intestatario: 'Pizzeria da Luigi Srl', partitaIvaCF: '01234560899', tipo: 'FATTURA', totale: 230.50 }
  ]);

  nuovaFattura = signal({
    ordineId: null,
    intestatario: '',
    partitaIvaCF: '',
    codiceSdiPec: ''
  });


  //Instanze Chart.js (per grafici)
  private chartAndamentoInstance: any = null;
  private chartMetodiInstance: any = null;
  ngOnInit(): void {

  }

  ngAfterViewInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      setTimeout(() => {
        this.initGraficoAndamento();
        this.initGraficoMetodi();
      }, 50);
    }
  }

  changePeriod(periodo: 'OGGI' | 'SETTIMANA' | 'MESE'): void {
    this.periodoSelezionato.set(periodo);

    this.aggiornaGrafici();
  }

  initGraficoAndamento(): void {
    const canvas = document.getElementById('andamentoIncassiChart') as HTMLCanvasElement;
    if (!canvas) return;

    if (this.chartAndamentoInstance) {
      this.chartAndamentoInstance.destroy();
    }

    this.chartAndamentoInstance = new Chart(canvas, {
      type: 'line',
      data: {
        labels: ['11:00', '13:00', '15:00', '17:00', '19:00', '21:00', '23:00'],
        datasets: [{
          label: 'Incassi (€)',
          data: [50, 420, 180, 90, 310, 650, 240],
          borderColor: '#10b981',
          backgroundColor: 'rgba(16, 185, 129, 0.1)',
          fill: true,
          tension: 0.3,
          borderWidth: 3
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false }
        },
        scales: {
          y: { beginAtZero: true, grid: { color: '#f1f5f9' } },
          x: { grid: { display: false } }
        }
      }
    });
  }

  initGraficoMetodi(): void {
    const canvas = document.getElementById('metodiContabilitaChart') as HTMLCanvasElement;
    if (!canvas) return;

    if (this.chartMetodiInstance) {
      this.chartMetodiInstance.destroy();
    }

    this.chartMetodiInstance = new Chart(canvas, {
      type: 'doughnut',
      data: {
        labels: ['Contanti', 'POS / Carta'],
        datasets: [{
          data: [450, 780.40],
          backgroundColor: ['#f59e0b', '#3b82f6'],
          borderWidth: 2,
          borderColor: '#ffffff'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { position: 'bottom' }
        }
      }
    });
  }

  aggiornaGrafici(): void {
    if (isPlatformBrowser(this.platformId)) {
      setTimeout(() => {
        this.initGraficoAndamento();
        this.initGraficoMetodi();
      }, 50);
    }
  }

  emettiDocumentoFiscale(): void {
    const dati = this.nuovaFattura();
    if (!dati.intestatario || !dati.partitaIvaCF) {
      this.snackBar.open('Compila i campi obbligatori dellintestatario.', 'Chiudi', { duration: 3000 });
      return;
    }

    // Simulazione aggiunta documento
    const nuovoDoc: DocumentoFiscale = {
      id: Date.now(),
      numeroDocumento: `FAT-2026/0${this.documenti().length + 1}`,
      dataEmissione: new Date().toISOString(),
      intestatario: dati.intestatario,
      partitaIvaCF: dati.partitaIvaCF,
      tipo: 'FATTURA',
      totale: 120.00 // Valore preso dall'ordine selezionato
    };

    this.documenti.update(docs => [nuovoDoc, ...docs]);
    this.numeroFattureEmesse.update(n => n + 1);

    this.snackBar.open('Fattura emessa con successo!', 'Chiudi', { duration: 3000 });

    // Reset form e chiusura modale (se gestita via TS o bootstrap)
    this.nuovaFattura.set({ ordineId: null, intestatario: '', partitaIvaCF: '', codiceSdiPec: '' });
  }

  stampaDocumento(doc: DocumentoFiscale): void {
    this.snackBar.open(`Generazione stampa per ${doc.numeroDocumento}...`, 'Chiudi', { duration: 2000 });
    // Integrazione futura con servizi di stampa o download PDF
  }
}
