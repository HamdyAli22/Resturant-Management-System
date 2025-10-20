import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class RequestOrderService {

  url = 'http://localhost:8081/orders/create-orders';

  constructor(private http: HttpClient) { }

  createOrder(productsIds: any, totalPrice: any, totalNumber: any): Observable<any> {
    return this.http.post<any>(this.url, {productsIds, totalPrice, totalNumber}).pipe(
      map(
        response => response
      )
    );
  }

}
