import { Component, OnInit } from '@angular/core';
import {AccountDetails} from '../../../../model/account-details';
import {AccountService} from '../../../../service/account.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-account-details',
  templateUrl: './account-details.component.html',
  styleUrls: ['./account-details.component.css']
})
export class AccountDetailsComponent implements OnInit {
  details: AccountDetails = new AccountDetails();
  messageAr = '';
  messageEn = '';
  username: string = localStorage.getItem('userName') || '';

  constructor(private accountService: AccountService, private router: Router) { }

  ngOnInit(): void {
  }

  saveDetails(): void {
    this.accountService.updateAccountDetails(this.username, this.details).subscribe({
      next: (response: any) => {
        this.messageAr = 'تم حفظ تفاصيل الحساب بنجاح.';
        this.messageEn = 'Account details saved successfully.';

        setTimeout(() => {
          this.router.navigate(['/products']);
        }, 2000);
      },
      error: errorResponse => {
        this.messageEn =  errorResponse.error[0].en;
        this.messageAr =  errorResponse.error[0].ar;
      }
    });
    this.autoClearMessage();
  }

  clearMessage(): void {
    this.messageEn = '';
    this.messageAr = '';
  }

  autoClearMessage(): void {
    setTimeout(() => this.clearMessage(), 5000);
  }
}
