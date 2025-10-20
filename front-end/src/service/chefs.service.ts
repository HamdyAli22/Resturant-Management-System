import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Chefs} from '../model/chefs';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ChefsService {

  chefUrl = 'http://localhost:8081/chefs';
  constructor(private http: HttpClient) { }

  getChefs(): Observable<Chefs[]> {
    return this.http.get<Chefs[]>(this.chefUrl).pipe(
      map(response => response)
    );
  }
}
