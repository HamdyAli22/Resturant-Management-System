import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {OrdersResponse} from '../model/user-order-response';
import {Order} from '../model/order';


@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private baseUrl = 'http://localhost:8081/orders/';

  constructor(private http: HttpClient) { }

  getUserOrders(pageNo, pageSize): Observable<OrdersResponse> {
    return this.http.get<OrdersResponse>(this.baseUrl + 'user-orders?' + 'page=' + pageNo + '&size=' + pageSize);
  }

  getAllOrders(pageNo, pageSize, username?: string): Observable<OrdersResponse> {
    let url = this.baseUrl + 'all-orders?' + 'page=' + pageNo + '&size=' + pageSize;
    if (username && username.trim() !== '') {
      url += '&username=' + username.trim();
    }
    return this.http.get<OrdersResponse>(url); // for admin
  }

  getOrderById(id: number): Observable<Order> {
    return this.http.get<Order>(this.baseUrl + 'get-order?id=' + id);
  }

  deleteOrder(id: number): Observable<any> {
    return this.http.delete(this.baseUrl + 'delete-order?id=' + id);
  }

  searchUserOrders(keyword: string, pageNo: number, pageSize: number): Observable<OrdersResponse> {
    const url = this.baseUrl + 'search-orders?' + 'keyword=' + keyword + '&page=' + pageNo + '&size=' + pageSize;
    return this.http.get<OrdersResponse>(url);
  }



}
