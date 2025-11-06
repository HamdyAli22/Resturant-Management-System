import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../../../service/auth.service';
import {NotificationService} from '../../../service/notification.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit{
  unreadCount = 0;
  showNotifications = false;
  username: string;

  constructor(private router: Router,
              private authService: AuthService,
              private notificationService: NotificationService) {
  }

  ngOnInit(): void {
    this.username = localStorage.getItem('userName') || '';
    if (this.username) {
      this.loadUnreadCount();
      setInterval(() => this.loadUnreadCount(), 10000);
    }

    this.authService.userLoggedIn.subscribe(username => {
      this.username = username;
      this.loadUnreadCount();
      setInterval(() => this.loadUnreadCount(), 10000);
    });

  }

  loadUnreadCount(): void {
    if (!this.username) { return; }
    this.notificationService.getUnreadByUser(this.username).subscribe({
      next: (data) => (this.unreadCount = data.length),
      error: () => (this.unreadCount = 0)
    });
  }

  toggleNotifications(): void {
    this.showNotifications = !this.showNotifications;
  }

  search(key: string): void {
   this.router.navigateByUrl('/products/' + key);
  }

  isUserLogin(): boolean{
    return this.authService.isUserLogin();
  }

  showCard(): boolean {
    return this.isUserLogin() && !this.router.url.startsWith('/dashboard');
  }

  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/login');
  }

  showSearch(): boolean {
    return this.isUserLogin() && (this.router.url.startsWith('/products') || this.router.url.startsWith('/category'));
  }

  isAdmin(): boolean{
    return this.authService.isAdmin();
  }

}
