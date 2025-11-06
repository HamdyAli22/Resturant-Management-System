import { Component, OnInit } from '@angular/core';
import { validate } from 'codelyzer/walkerFactory/walkerFn';
import {AuthService} from '../../../service/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  messageAr = '';
  messageEn = '';
  showPassword = false;
  showConfirmPassword = false;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  createAccount = (username: string, password: string, confirmPassword: string): void => {

    if (!this.validateAccount(username, password, confirmPassword)) {
      this.autoClearMessage();
      return;
    }
    this.authService.createAccount(username, password).subscribe(
      response => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('userName', response.username);
        localStorage.setItem('roles', response.roles);
        this.router.navigateByUrl('/products');
      },
      errorResponse => {
        if (Array.isArray(errorResponse.error)) {
          this.messageAr = errorResponse.error.map((e: any) => e.ar).join('، ');
          this.messageEn = errorResponse.error.map((e: any) => e.en).join(', ');
        } else {
          this.messageAr = errorResponse.error.ar;
          this.messageEn = errorResponse.error.en;
        }
        this.autoClearMessage();
      }
    );

  }

  validateAccount(username: string, password: string, confirmPassword: string): boolean {

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

    if (!confirmPassword) {
      this.messageEn = 'Confirm password is required.';
      this.messageAr = 'تأكيد كلمة المرور مطلوب.';
      return false;
    }

    if (password !== confirmPassword) {
      this.messageEn = 'Password and confirm password do not match.';
      this.messageAr = 'كلمة المرور وتأكيد كلمة المرور غير متطابقين.';
      return false;
    }
    return true;
  }

  togglePasswordVisibility(field: 'password' | 'confirm'): void {
    if (field === 'password') {
      this.showPassword = !this.showPassword;
    } else {
      this.showConfirmPassword = !this.showConfirmPassword;
    }
  }

  clearMessage(): void {
    this.messageEn = '';
    this.messageAr = '';
  }

  autoClearMessage(): void {
    setTimeout(() => this.clearMessage(), 5000);
  }
}
