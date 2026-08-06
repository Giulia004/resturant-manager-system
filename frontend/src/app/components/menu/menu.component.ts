import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { PiattiService, Piatto } from '../../services/piatti.service';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-menu',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css',
})

export class MenuComponent implements OnInit {
  private menuService = inject(PiattiService);

  form: FormGroup;
  piatti = signal<Piatto[]>([]);
  loading = signal<boolean>(true);

  isAdmin: boolean = false;

  ordineCorrente = signal<Piatto[]>([]);

  categorieDisponibili: string[] = ['Tutti', 'Antipasti', 'Primi', 'Secondi', 'Pizze', 'Bevande'];
  categoriaSelezionata = signal<string>('Tutti');

  menuFiltrato = computed(() => {
    const cat = this.categoriaSelezionata();
    const lista = this.piatti();
    if (cat === 'Tutti') return lista;
    return lista.filter(p => p.categoria === cat.toUpperCase());
  });

  constructor(private fb: FormBuilder, private authService: AuthService) {
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
  }

  verifyRole(): void {
    const userRole = this.authService.getRole();
    const roleStr = userRole as unknown as string;
    this.isAdmin = roleStr === 'ADMIN' || roleStr === 'ROLE_ADMIN';
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

    const newPiatto: Piatto = this.form.value;

    this.menuService.createPaitto(newPiatto).subscribe({
      next: () => {
        this.piatti.update(list => [...list, { ...newPiatto, id: Date.now() }]);
        this.form.reset({ categoria: 'Antipasti', disponibile: true });
      }, error: (err) => console.log(err)
    });
  }

  eliminaPiatto(id: number): void {

  }

  addItem(piatto: Piatto): void {
    this.ordineCorrente.update(list => [...list, piatto]);
    console.log("Piatto aggiunto");
  }

  selezionaCategoria(categoria: string): void {
    this.categoriaSelezionata.set(categoria);
  }
}
