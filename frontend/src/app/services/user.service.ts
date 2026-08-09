import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

export type Ruolo = 'ADMIN' | 'CAMERIERE' | 'CUOCO' | 'CASSIERE';

export interface User{
    id?: number;
    username: string;
    ruolo: Ruolo;
};

export interface CreateUserRequest{
    username: string;
    password: string;
    ruolo: Ruolo;
};

@Injectable({
    providedIn:'root'
})

export class UserService{
    private readonly API = "http://localhost:8000/api/users";

    constructor(private http: HttpClient) { }

    getAll(): Observable<User[]>{
        return this.http.get<User[]>(this.API);
    }

    getById(id:number): Observable<User>{
        return this.http.get<User>(`${this.API}/${id}`);
    }

    createUser(data: CreateUserRequest): Observable<User>{
        return this.http.post<User>(this.API, data);
    }
    updateUser(id: number, data: Partial<Pick<User,'username'|'ruolo'>>): Observable<User>{
        return this.http.put<User>(`${this.API}/${id}`, data);
    }

    deleteUser(id: number): Observable<any>{
        return this.http.delete(`${this.API}/${id}`);
    }
}