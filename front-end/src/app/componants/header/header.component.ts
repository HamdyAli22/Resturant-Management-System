import { Component } from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../../../service/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  constructor(private router: Router, private authService: AuthService) {
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
