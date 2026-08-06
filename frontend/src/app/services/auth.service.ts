import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

export interface RegisterRequest{
    username: string;
    password: string;
    ruolo: string;
};

export interface LoginResponse{
    token: string;
    ruolo: string;
};

@Injectable({
    providedIn:'root'
})

export class AuthService{
    private readonly API = 'http://localhost:8000/api/auth';
    

    constructor(private http: HttpClient) { }
    
    register(data: RegisterRequest): Observable<any>{
        return this.http.post(`${this.API}/register`, data);
    }

    login(username: string, password: string): Observable<LoginResponse>{
        return this.http.post<LoginResponse>(`${this.API}/login`, { username, password });
    }
}