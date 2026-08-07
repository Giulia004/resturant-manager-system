import { Component, computed, inject, OnInit, PLATFORM_ID, signal } from '@angular/core';
import { PiattiService, Piatto } from '../../services/piatti.service';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { OrdineService } from '../../services/ordine.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-menu',
  imports: [CommonModule, ReactiveFormsModule, MatSnackBarModule],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css',
})

export class MenuComponent implements OnInit {
  private menuService = inject(PiattiService);
  private ordineService = inject(OrdineService);
  private snackBar = inject(MatSnackBar);
  private authService = inject(AuthService);
  private fb = inject(FormBuilder);
  private platformId = inject(PLATFORM_ID);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  form: FormGroup;
  piatti = signal<Piatto[]>([]);
  loading = signal<boolean>(true);

  isAdmin: boolean = false;

  numeroTavolo = signal<number | null>(null);

  ordineCorrente = signal<Piatto[]>([]);

  categorieDisponibili: string[] = ['Tutti', 'Antipasti', 'Primi', 'Secondi', 'Pizze', 'Bevande'];
  categoriaSelezionata = signal<string>('Tutti');

  menuFiltrato = computed(() => {
    const cat = this.categoriaSelezionata();
    const lista = this.piatti();
    if (cat === 'Tutti') return lista;
    return lista.filter(p => p.categoria === cat.toUpperCase());
  });

  constructor() {
    this.form = this.fb.group({
      nome: ['', Validators.required],
      descrizione: [''],
      prezzo: [null, [Validators.required, Validators.min(0)]],
      categoria: ['Antipasti', Validators.required],
      disponibile: [true]
    });
  }

  ngOnInit(): void {
    this.loadMenu();
    this.verifyRole();
    this.verifyTable();
  }

  verifyRole(): void {
    const userRole = this.authService.getRole();
    const roleStr = userRole as unknown as string;
    this.isAdmin = roleStr === 'ADMIN' || roleStr === 'ROLE_ADMIN';
  }

  verifyTable(): void {
    const tavoloParam = this.route.snapshot.queryParamMap.get('tavolo');
    const tavolo = tavoloParam ? Number(tavoloParam) : null;

    if (!tavolo || tavolo <= 0) {
      this.snackBar.open('Numero tavolo mancante: seleziona un tavolo per iniziare una comanda.', 'Chiudi', { duration: 3000 });
      this.router.navigate(['/ordini']);
      return;
    }

    this.numeroTavolo.set(tavolo);
  }

  loadMenu(): void {
    this.menuService.getPiatti().subscribe({
      next: (res) => {
        this.piatti.set(res);
        this.loading.set(false);
      }, error: (err) => {
        this.loading.set(false);
        console.log(err)
      }
    });
  }

  creaPiatto() {
    if (!this.isAdmin) {
      console.warn('Accesso negato: solo gli amministratori possono creare nuovi piatti.');
      return;
    }

    if (this.form.invalid) return;

    const formValue = this.form.value;
    const newPiatto: Piatto = {
      ...formValue,
      categoria: formValue.categoria.toUpperCase()
    };

    this.menuService.createPiatto(newPiatto).subscribe({
      next: () => {
        this.loadMenu();
        //this.piatti.update(list => [...list, { ...newPiatto, id: Date.now() }]);
        this.form.reset({ categoria: 'Antipasti', disponibile: true });
      }, error: (err) => console.log(err)
    });
  }

  eliminaPiatto(id: number): void {
    if (!confirm("Eliminare questo piatto?")) return;

    this.menuService.deletePiatto(id).subscribe({
      next: () => {
        this.piatti.update((p) => this.piatti().filter(p => p.id !== id));
        this.snackBar.open('Tavolo eliminato.', 'Chiudi', { duration: 2000 });
      }, error: (err) => {
        this.snackBar.open('Errore durante l\'eliminazione.', 'Chiudi', { duration: 3000 });
        console.error(err);
      }
    });
  }

  addItem(piatto: Piatto): void {
    this.ordineCorrente.update(list => [...list, piatto]);
  }

  removeItem(piatto: Piatto): void {
    this.ordineCorrente.update(list => list.filter((_, i) => i !== piatto.id));
  }

  calcolaTotaleOrdine(): number {
    return this.ordineCorrente().reduce((acc, piatto) => acc + piatto.prezzo, 0);
  }

  selezionaCategoria(categoria: string): void {
    this.categoriaSelezionata.set(categoria);
  }

  async apriRiepilogoOrdine(): Promise<void> {
    if (isPlatformBrowser(this.platformId)) {
      const bootstrap = await import('bootstrap');

      const modalElement = document.getElementById('riepilogoModal');
      if (modalElement) {
        const modal = new bootstrap.Modal(modalElement);
        modal.show();
      }
    }
  }

  inviaComanda(): void {
    const elementiCorrenti = this.ordineCorrente();

    if (elementiCorrenti.length === 0) return;

    //I duplicati vengono aggregati in un'unica riga con la relativa quantità
    const righeMap = new Map<number, number>();
    for (const piatto of elementiCorrenti) {
      righeMap.set(piatto.id!, (righeMap.get(piatto.id!) || 0) + 1);
    }

    const righe = Array.from(righeMap.entries()).map(([piattoId, qta]) => ({ piattoId, qta }));

    const numeroTavolo = this.numeroTavolo();
    if (numeroTavolo === null) {
      this.snackBar.open('Numero tavolo non valido.', 'Chiudi', { duration: 3000 });
      return;
    }

    const nuovoOrdine = {
      numeroTavolo,
      righe: righe
    };

    this.ordineService.createOrdine(nuovoOrdine).subscribe({
      next: () => {
        this.snackBar.open('Comanda creata con successo!', 'Chiudi', { duration: 2000 });
        this.ordineCorrente.set([]);
        this.router.navigate(['/ordini']);
      }, error: (err) => {
        this.snackBar.open('Errore durante il salvataggio dell\'ordine.', 'Chiudi', { duration: 3000 });
        console.error(err);
      }
    });
  }
}
