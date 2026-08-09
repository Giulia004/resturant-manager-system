import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CreateUserRequest, Ruolo, User, UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-staff-management',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatSnackBarModule,FormsModule],
  templateUrl: './staff-management.component.html',
  styleUrl: './staff-management.component.css',
})
export class StaffManagementComponent implements OnInit {
  private userService = inject(UserService);
  private authService = inject(AuthService);
  private fb = inject(FormBuilder);
  private snackBar = inject(MatSnackBar);

  utenti = signal<User[]>([]);
  loading = signal<boolean>(false);

  ruoli: Ruolo[] = ['ADMIN', 'CAMERIERE', 'CUOCO', 'CASSIERE'];

  form: FormGroup = this.fb.group({
    username: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(30)]],
    password: ['', [
      Validators.required,
      Validators.minLength(8),
      Validators.pattern(/^(?=.*[A-Z])(?=.*\d).+$/)
    ]],
    ruolo: ['', Validators.required],
  });

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading.set(true);

    this.userService.getAll().subscribe({
      next: (res) => {
        this.utenti.set(res);
        this.loading.set(false);
      }, error: () => {
        this.loading.set(false);
        this.snackBar.open('Errore nel caricamento dello staff.', 'Chiudi', { duration: 3000 });
      }
    });
  }

  creaUtente(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const request: CreateUserRequest = this.form.value;

    this.userService.createUser(request).subscribe({
      next: () => {
        this.snackBar.open('Membro dello staff creato con successo.', 'Chiudi', { duration: 3000 });
        this.form.reset();
        this.loadUsers();
      }, error: (err) => {
        const msg = err?.error?.error || 'Errore nella creazione dello staff.';
        this.snackBar.open(msg, 'Chiudi', { duration: 3000 });
      }
    });
  }

  cambiaRuolo(utente: User, nuovoRuolo: Ruolo): void {
    if (nuovoRuolo === utente.ruolo || !utente.id) return;

    this.userService.updateUser(utente.id, { ruolo: nuovoRuolo }).subscribe({
      next: (aggiornato) => {
        utente.ruolo = aggiornato.ruolo;
        this.snackBar.open('Ruolo aggiornato.', 'Chiudi', { duration: 2000 });
      },
      error: () => {
        this.snackBar.open('Errore nell\'aggiornamento del ruolo.', 'Chiudi', { duration: 3000 });
      }
    });
  }

  eliminaUtente(utente: User): void {
    if (!utente.id) return;

    //L'utente stesso non può autoeliminarsi
    if (utente.username === this.authService.getUsername()) {
      this.snackBar.open('Non puoi eliminare il tuo stesso account.', 'Chiudi', { duration: 3000 });
      return;
    }

    if (!confirm(`Eliminare l'utente ${utente.username}?`)) return;

    this.userService.deleteUser(utente.id).subscribe({
      next: () => {
        this.utenti.set(this.utenti().filter(u => u.id !== utente.id));
        this.snackBar.open('Utente eliminato.', 'Chiudi', { duration: 2000 });
      },
      error: () => {
        this.snackBar.open('Errore nell\'eliminazione dell\'utente.', 'Chiudi', { duration: 3000 });
      }
    });
  }
}
