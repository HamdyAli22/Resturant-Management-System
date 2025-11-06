import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../../service/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  messageAr = '';
  messageEn = '';
  showPassword = false;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  login(username: string, password: string): void {

    if (!this.validateAccount(username, password)) {
      this.autoClearMessage();
      return;
    }

    this.authService.login(username, password).subscribe(
      response => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('userName', response.username);
        localStorage.setItem('roles', response.roles);
        this.router.navigateByUrl('/products');
      },
      error => {
        if (Array.isArray(error.error)) {
          this.messageAr = error.error.map((e: any) => e.ar).join('، ');
          this.messageEn = error.error.map((e: any) => e.en).join(', ');
        } else {
          this.messageAr = error.error.ar;
          this.messageEn = error.error.en;
        }
        this.autoClearMessage();
      }
    );

  }

  validateAccount(username: string, password: string): boolean {

    if (!username) {
      this.messageEn = 'Username is required.';
      this.messageAr = 'اسم المستخدم مطلوب.';
      return false;
    }

    if (!password) {
      this.messageEn = 'Password is required.';
      this.messageAr = 'كلمة المرور مطلوبة.';
      return false;
    }
    return true;
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  clearMessage(): void {
    this.messageEn = '';
    this.messageAr = '';
  }

  autoClearMessage(): void {
    setTimeout(() => this.clearMessage(), 5000);
  }

}
