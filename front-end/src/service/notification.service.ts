import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Notification} from '../model/notification';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private baseUrl = 'http://localhost:8081/notifications';

  constructor(private http: HttpClient) { }

  getAllByUser(username: string): Observable<Notification[]> {
    return this.http.get<Notification[]>(this.baseUrl + '/user?username=' + username);
  }

  getUnreadByUser(username: string): Observable<Notification[]> {
    return this.http.get<Notification[]>(this.baseUrl + '/user/unread?username=' + username);
  }

  markAsRead(notificationId: number): Observable<void> {
    return this.http.put<void>(this.baseUrl + '/mark-read?id=' + notificationId, {});
  }

  deleteNotification(id: number): Observable<void> {
    return this.http.delete<void>(this.baseUrl + '/delete?id=' + id);
  }

  markAllAsRead(username: string): Observable<void> {
    return this.http.put<void>(this.baseUrl + '/mark-all-read?username=' + username, {});
  }

}
