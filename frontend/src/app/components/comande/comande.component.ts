import { Component, inject, OnInit, PLATFORM_ID, signal } from '@angular/core';
import { Ordine, OrdineService, StatoOrdine } from '../../services/ordine.service';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-comande',
  imports: [CommonModule, MatSnackBarModule, FormsModule],
  templateUrl: './comande.component.html',
  styleUrl: './comande.component.css',
})
export class ComandeComponent implements OnInit {
  private ordiniService = inject(OrdineService);
  private authService = inject(AuthService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);
  private platformId = inject(PLATFORM_ID);

  public orders = signal<Ordine[]>([]);

  loading = signal<boolean>(true);
  numeroTavolo: number | null = null;
  tavoloNonValido = signal<boolean>(false);

  statiDisponibili: StatoOrdine[] = ['IN_ATTESA', 'IN_PREPARAZIONE', 'PRONTO', 'SERVITO', 'PAGATO', 'ANNULLATO'];

  isAuthorize: boolean = false;
  
  ngOnInit(): void {
    this.loadOrders();
    this.verifyRole();
  }

  verifyRole(): void {
    const userRole = this.authService.getRole();
    const roleStr = userRole as unknown as string;

    if (roleStr === 'ADMIN' || roleStr === 'ROLE_ADMIN') this.isAuthorize = true;
    if (roleStr === 'CUOCO' || roleStr === 'ROLE_CUOCO') this.isAuthorize = true;

  }

  loadOrders(): void {
    this.ordiniService.getAll().subscribe({
      next: (res) => {
        this.orders.set(res);
        this.loading.set(false);
      }, error: (err) => this.loading.set(false)
    });
  }

  async apriNuovaComanda(): Promise<void> {
    this.numeroTavolo = null;
    this.tavoloNonValido.set(false);

    if (isPlatformBrowser(this.platformId)) {
      const bootstrap = await import('bootstrap');
      const modalElement = document.getElementById('nuovaComandaModal');
      if (modalElement) {
        const modal = new bootstrap.Modal(modalElement);
        modal.show();
      }
    }
  }

  async confermaNuovaComanda(): Promise<void> {
    if (!this.numeroTavolo || this.numeroTavolo <= 0) {
      this.tavoloNonValido.set(true);
      return;
    }

    if (isPlatformBrowser(this.platformId)) {
      const bootstrap = await import('bootstrap');
      const modalElement = document.getElementById('nuovaComandaModal');
      if (modalElement) {
        const modal = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
        modal.hide();
      }
    }

    this.router.navigate(['/menu'], { queryParams: { tavolo: this.numeroTavolo } });
  }

  async chiudiComanda(): Promise<void> {
    const bootstrap = await import('bootstrap');
    const modalElement = document.getElementById('nuovaComandaModal');
    if (modalElement) {
      const modal = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
      modal.hide();
    }
  }

  cambiaStato(ordine: Ordine, nuovoStato: String): void {
    const stato = nuovoStato as StatoOrdine;
    if (stato === ordine.stato) return;

    this.ordiniService.updateStato(ordine.id!, stato).subscribe({
      next: (update) => {
        this.orders.update(list => list.map(o => o.id === update.id ? update : o));
        this.snackBar.open('Stato aggiornato.', 'Chiudi', { duration: 2000 });
      }, error: (err) => {
        this.snackBar.open('Errore durante l\'aggiornamento dello stato.', 'Chiudi', { duration: 3000 });
        console.error(err);
      }
    })
  }

  eliminaComanda(id: number): void {
    if (!confirm("Eliminare la comanda?")) return;

    this.ordiniService.delete(id).subscribe({
      next: () => {
        this.orders.update(list => list.filter(o => o.id !== id));
        this.snackBar.open('Comanda eliminata.', 'Chiudi', { duration: 2000 });
      }, error: (err) => {
        this.snackBar.open('Errore durante l\'eliminazione.', 'Chiudi', { duration: 3000 });
        console.error(err);
      }
    });
  }

  back(): void {
    this.router.navigate(['/dashboard']);
  }
}
