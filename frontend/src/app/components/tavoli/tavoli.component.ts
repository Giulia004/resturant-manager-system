import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { TavoliService, Tavolo } from '../../services/tavoli.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-tavoli',
  imports: [CommonModule, ReactiveFormsModule, MatSnackBarModule],
  templateUrl: './tavoli.component.html',
  styleUrl: './tavoli.component.css',
})
export class TavoliComponent implements OnInit {
  private tavoliService = inject(TavoliService);
  private authService = inject(AuthService);
  private snackBar = inject(MatSnackBar)
  private fb = inject(FormBuilder);

  public tavoli = signal<Tavolo[]>([]);
  loading = signal(true);
  form: FormGroup;

  constructor() {
    this.form = this.fb.group({
      numero: ['', [Validators.required, Validators.min(1)]],
      posti: ['', [Validators.required, Validators.min(1)]],
    });
  }

  ngOnInit(): void {
    this.caricaTavoli();
  }

  get isAdmin(): boolean {
    return this.authService.getRole() === 'ADMIN';
  }

  caricaTavoli(): void {
    this.loading.set(true);

    this.tavoliService.getTavoli().subscribe({
      next: (data) => {
        this.tavoli.set(data);
        this.loading.set(false);
      }, error: (err) => {
        this.loading.set(false);
        this.snackBar.open('Errore nel caricamento dei tavoli.', 'Chiudi', { duration: 3000 });
        console.error(err);
      }
    });
  }

  creaTavolo(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const nuovoTavolo: Tavolo = {
      numero: this.form.value.numero,
      posti: this.form.value.posti,
      disponibile: true
    };

    this.tavoliService.createTavolo(nuovoTavolo).subscribe({
      next: (tavolo) => {
        this.tavoli.update((tavoli) => [...tavoli, tavolo]);
        this.form.reset();
        this.snackBar.open('Tavolo creato.', 'Chiudi', { duration: 2000 });
      }, error: (err) => {
        this.snackBar.open('Errore durante la creazione.', 'Chiudi', { duration: 3000 });
        console.error(err);
      }
    });
  }

  toggleDisponibilita(tavolo: Tavolo): void {
    const aggiornato = { disponibile: !tavolo.disponibile };

    this.tavoliService.updateTavolo(tavolo.id!, aggiornato).subscribe({
      next: (res) => this.tavoli.update(list=>list.map(t=>t.id===res.id? res :t)),
      error: (err) => {
        this.snackBar.open('Errore durante l\'aggiornamento.', 'Chiudi', { duration: 3000 });
        console.error(err);
      }
    });
  }

  eliminaTavolo(id: number): void {
    if (!confirm('Eliminare questo tavolo?')) return;

    this.tavoliService.deleteTavolo(id).subscribe({
      next: () => {
        this.tavoli.update((tavoli) => tavoli.filter(t => t.id !== id));
        this.snackBar.open('Tavolo eliminato.', 'Chiudi', { duration: 2000 });
      },
      error: (err) => {
        this.snackBar.open('Errore durante l\'eliminazione.', 'Chiudi', { duration: 3000 });
        console.error(err);
      }
    });
  }
}
