import { Component, OnInit } from '@angular/core';
import {AccountService} from '../../../../service/account.service';
import { Account } from '../../../../model/account';
@Component({
  selector: 'app-disable-account',
  templateUrl: './disable-account.component.html',
  styleUrls: ['./disable-account.component.css']
})
export class DisableAccountComponent implements OnInit {

  accounts: Account[] = [];
  messageAr = '';
  messageEn = '';

  constructor(private accountService: AccountService) { }

  ngOnInit(): void {
    this.loadAccounts();
  }

  loadAccounts(): void {
    this.accountService.getAllAccounts().subscribe({
      next: (data) => {
        this.accounts = data;
      },
      error: errorResponse => {
        this.messageEn = errorResponse.error.en;
        this.messageAr = errorResponse.error.ar;
      }
    });
  }

  toggleStatus(account: Account): void {
    this.accountService.toggleAccountStatus(account.id).subscribe({
      next: (response) => {
        account.enabled = !account.enabled;
        this.messageEn = response.en;
        this.messageAr = response.ar;
        this.autoClearMessage();
      },
      error: errorResponse => {
        this.messageEn = errorResponse.error.en;
        this.messageAr = errorResponse.error.ar;
        this.autoClearMessage();
      }
    });
  }

  clearMessage(): void {
    this.messageEn = '';
    this.messageAr = '';
  }

  autoClearMessage(): void {
    setTimeout(() => this.clearMessage(), 3000);
  }

}
