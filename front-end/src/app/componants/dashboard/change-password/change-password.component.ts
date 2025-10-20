import { Component, OnInit } from '@angular/core';
import {ChangePassReq} from '../../../../model/change-pass-req';
import {AccountService} from '../../../../service/account.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  form: ChangePassReq = new ChangePassReq();
  mode: 'dashboard' | 'forgot' = 'dashboard';
  messageAr = '';
  messageEn = '';
  isSuccess = false;
  isLoading = false;

  constructor( private accountService: AccountService,
               private route: ActivatedRoute,
               private router: Router
             ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (params.mode === 'forgot') {
        this.mode = 'forgot';
      }
    });
  }

  onSubmit(): void {
    this.isLoading = true;
    this.messageAr = '';
    this.messageEn = '';
    this.isSuccess = false;

    const request = this.mode === 'forgot'
      ? this.accountService.resetPassword(this.form)
      : this.accountService.changePassword(this.form);

    request.subscribe({
      next: res => {
        this.isLoading = false;
        this.isSuccess = true;
        this.messageAr = res.ar;
        this.messageEn = res.en;

        // نفرّغ الفورم
        this.form = new ChangePassReq();

        localStorage.clear();
        sessionStorage.clear();

        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: errorResponse => {
        this.isLoading = false;
        this.isSuccess = false;
        if (Array.isArray(errorResponse.error)) {
          this.messageAr = errorResponse.error[0].ar;
          this.messageEn = errorResponse.error[0].en;
        } else {
          this.messageAr = errorResponse.error.ar;
          this.messageEn = errorResponse.error.en;
        }
      }
    });
  }

}
