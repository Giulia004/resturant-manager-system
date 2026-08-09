import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';

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
  public userService = inject(UserService);
  public effectiveName: string | undefined;

  constructor(private router: Router) { }

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }
    this.userRole = this.authService.getRole() || '';
    this.username = this.authService.getUsername() || '';

    this.effectiveName = this.username.split('.').map(part => part.charAt(0).toUpperCase() + part.slice(1)).join(' ');
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

}
