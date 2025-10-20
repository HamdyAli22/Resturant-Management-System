import { Component } from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../service/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private authService: AuthService, private router: Router) {
  }

  isUserLogin(): boolean{
    return this.authService.isUserLogin();
  }

  showCategory(): boolean{
    return this.isUserLogin() && !this.router.url.startsWith('/dashboard');
  }
}
