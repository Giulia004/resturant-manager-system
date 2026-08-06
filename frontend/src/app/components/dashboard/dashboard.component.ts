import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class Dashboard implements OnInit {
  userRole: string = '';
  username: string = '';

  public authService = inject(AuthService);

  constructor(private router: Router) { }

  ngOnInit(): void {
    this.userRole = localStorage.getItem('role') || 'ADMIN';
    this.username = localStorage.getItem('username') || 'Utente';
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/home']);
  }

}
