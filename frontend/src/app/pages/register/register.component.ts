import { CommonModule } from '@angular/common';
import { Component, Injectable } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatSnackBarModule,
    RouterLink
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})

export class RegisterComponent {
  form: FormGroup;
  loading = false;

  roles = ['ADMIN', 'CAMERIERE', 'CUOCO', 'CASSIERE'];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.form = this.fb.group(
      {
        username: ['', [Validators.required, Validators.minLength(3)]],
        password: ['', [Validators.required, Validators.minLength(6)]],
        confermaPassword: ['', Validators.required],
        ruolo: ['', Validators.required]
      },
      { validators: this.passwordMatchValidator }
    );
  }

  passwordMatchValidator(group: AbstractControl): ValidationErrors | null {
    const password = group.get('password')?.value;
    const conferma = group.get('confermaPassword')?.value;
    return password === conferma ? null : { passwordMismatch: true };
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    const { username, password, ruolo } = this.form.value;

    this.authService.register({ username, password, ruolo }).subscribe({
      next: () => {
        this.loading = false;
        this.snackBar.open('Registrazione completata! Ora puoi accedere.', 'Chiudi', { duration: 3000 });
        this.router.navigate(['/dashboard']);
      },
      error: (err => {
        this.loading = false;
        this.snackBar.open('Errore durante la registrazione. Riprova.', 'Chiudi', { duration: 3000 });
        console.error(err);
      })
    });
  }
}