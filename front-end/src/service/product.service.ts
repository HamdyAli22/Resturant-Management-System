import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {Product} from '../model/product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  baseUrl = 'http://localhost:8081/products/';
  constructor(private http: HttpClient) {
  }

  getProducts(pageNo, pageSize): Observable<any> {
    return this.http.get<Product[]>(this.baseUrl + 'getAll?page=' + pageNo + '&size=' + pageSize).pipe(
      map(
        response => response
      )
    );
  }

  getProductsByCategoryId(id, pageNo, pageSize): Observable<any> {
    return this.http.get<Product[]>(this.baseUrl + 'searchByCategory/' + id + '?page=' + pageNo + '&size=' + pageSize).pipe(
      map(
        response => response
      )
    );
  }

  searchByKey(key, pageNo, pageSize): Observable<any> {
    return this.http.get<Product[]>(this.baseUrl + 'search?key=' + key + '&page=' + pageNo + '&size=' + pageSize).pipe(
      map(
        response => response
      )
    );
  }

  addProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(this.baseUrl + `save-product`, product);
  }


  updateProduct(product: Product): Observable<Product> {
    return this.http.put<Product>(this.baseUrl + 'update-product', product);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(this.baseUrl + 'delete?id=' + id);
  }
}
