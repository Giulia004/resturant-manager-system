import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Inject, Injectable, PLATFORM_ID } from "@angular/core";
import { Observable, tap } from "rxjs";
import { Ruolo } from "./user.service";
import { isPlatformBrowser } from "@angular/common";

export interface RegisterRequest {
    username: string;
    password: string;
    ruolo: Ruolo;
};

export interface LoginResponse {
    token: string;
    ruolo: Ruolo;
};

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private readonly API = 'http://localhost:8000/api/auth';
    private readonly TOKEN_KEY = 'auth_token';
    private readonly ROLE_KEY = 'auth_role';
    private readonly USERNAME_KEY = 'auth_username';
    private readonly isBrowser: boolean;

    constructor(
        private http: HttpClient,
        @Inject(PLATFORM_ID) platformId: Object
    ) {
        this.isBrowser = isPlatformBrowser(platformId);
    }

    login(username: string, password: string): Observable<LoginResponse> {
        return this.http.post<LoginResponse>(`${this.API}/login`, { username, password })
            .pipe(tap(res => {
                this.setToken(res.token);
                if (this.isBrowser) {
                    localStorage.setItem(this.ROLE_KEY, res.ruolo);
                    localStorage.setItem(this.USERNAME_KEY, username);
                }
            }));
    }

    logout(): void {
        if (this.isBrowser) {
            localStorage.removeItem(this.TOKEN_KEY);
            localStorage.removeItem(this.ROLE_KEY);
            localStorage.removeItem(this.USERNAME_KEY);
        }
    }

    getToken(): string | null {
        return this.isBrowser ? localStorage.getItem(this.TOKEN_KEY) : null;
    }

    getRole(): Ruolo | null {
        return this.isBrowser ? (localStorage.getItem(this.ROLE_KEY) as Ruolo | null) : null;
    }

    getUsername(): string | null {
        return this.isBrowser ? localStorage.getItem(this.USERNAME_KEY) : null;
    }

    isLoggedIn(): boolean {
        return !!this.getToken();
    }

    private setToken(token: string): void {
        if (this.isBrowser) {
            localStorage.setItem(this.TOKEN_KEY, token);
        }
    }

    getHeaders(): HttpHeaders{
        const token = this.getToken();
        return new HttpHeaders({
            'Content-Type': 'application/json',
            'Authorization': token ? `Bearer ${token}` : ''
        });
    }
}