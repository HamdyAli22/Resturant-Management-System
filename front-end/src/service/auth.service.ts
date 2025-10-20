import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Product} from '../model/product';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  baseUrl = 'http://localhost:8081/auth/';
  constructor(private http: HttpClient) {
  }

  createAccount(username: string, password: string): Observable<any>{
    return this.http.post<any>(this.baseUrl + 'signup', {username, password}).pipe(
      map(
        response => response
      )
    );
  }

  login(username: string, password: string): Observable<any>{
    return this.http.post<any>(this.baseUrl +  'login', {username, password}).pipe(
      map(
        response => response
      )
    );
  }

  isUserLogin(): boolean{
     return sessionStorage.getItem('token') !== null &&
       sessionStorage.getItem('token') !== undefined;
  }

  isAdmin(): boolean{
    const roles = sessionStorage.getItem('roles');
    console.log('Roles: ' + roles);
    if (!roles) {
      return false;
    }
    return roles.includes('ADMIN');
  }

  logout = () => {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('roles');
  }
}
