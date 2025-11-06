import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Category} from '../model/category';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  baseUrl = 'http://localhost:8081/categories';
  constructor(private http: HttpClient) {

  }

  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(this.baseUrl).pipe(
      map(
        response => response
      )
    );
  }

  getAllCategories(pageNo: number, pageSize: number): Observable<any> {
    return this.http.get<any>(this.baseUrl + '/getAll?page=' + pageNo + '&size=' + pageSize)
      .pipe(map(response => response));
  }

  searchByKey(keyword: string, pageNo: number, pageSize: number): Observable<any> {
    return this.http.get<any>(this.baseUrl + '/search?keyword=' + keyword + '&page=' + pageNo + '&size=' + pageSize)
      .pipe(map(response => response));
  }

  addCategory(category: Category): Observable<Category> {
    return this.http.post<Category>(this.baseUrl + '/save-category', category);
  }

  updateCategory(category: Category): Observable<Category> {
    return this.http.put<Category>(this.baseUrl + '/update-category', category);
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(this.baseUrl + '/delete?id=' + id);
  }
}
