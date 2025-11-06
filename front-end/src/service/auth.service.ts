import {EventEmitter, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Product} from '../model/product';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  baseUrl = 'http://localhost:8081/auth/';
  userLoggedIn = new EventEmitter<string>();

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
      map(response => {
        if (response?.token) {
          localStorage.setItem('token', response.token);
          localStorage.setItem('userName', response.username);
          localStorage.setItem('roles', response.roles);
          this.userLoggedIn.emit(username);
        }
        return response;
        }
      )
    );
  }

  isUserLogin(): boolean{
     return localStorage.getItem('token') !== null &&
       localStorage.getItem('token') !== undefined;
  }

  isAdmin(): boolean{
    const roles = localStorage.getItem('roles');
    console.log('Roles: ' + roles);
    if (!roles) {
      return false;
    }
    return roles.includes('ADMIN');
  }

  logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('roles');
  }
}
