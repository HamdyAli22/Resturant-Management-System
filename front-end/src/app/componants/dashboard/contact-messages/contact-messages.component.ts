import { Component, OnInit } from '@angular/core';
import {ContactInfo} from '../../../../model/contact-info';
import {ContactInfoService} from '../../../../service/contact-info.service';
import {AuthService} from '../../../../service/auth.service';

@Component({
  selector: 'app-contact-messages',
  templateUrl: './contact-messages.component.html',
  styleUrls: ['./contact-messages.component.css']
})
export class ContactMessagesComponent implements OnInit {
  messages: ContactInfo[] = [];
  messageAr = '';
  messageEn = '';
  pageNo = 1;
  pageSize = 10;
  totalMessages: number;
  searchKey = '';
  username: string = localStorage.getItem('userName') || '';
  searchKeyword = '';
  constructor(private contactService: ContactInfoService, private authService: AuthService) { }

  ngOnInit(): void {
    this.loadMessages();
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  loadMessages(keyword?: string): void {
    const isAdmin = this.isAdmin();

    if (keyword && keyword.trim() !== '') {
      this.contactService.searchMessages(keyword.trim(), this.pageNo, this.pageSize).subscribe({
        next: (data: any) => {
          this.messages = data.messages;
          this.totalMessages = data.totalMessages;
        },
        error: (errorResponse) => {
          this.messages = [];
          this.totalMessages = 0;
          this.handleError(errorResponse);
        }
      });
      return;
    }

    const obs = isAdmin
      ? this.contactService.getAllMessages(this.pageNo, this.pageSize)
      : this.contactService.getMessagesByUser(this.username, this.pageNo, this.pageSize);

    obs.subscribe({
      next: (data: any) => {
        this.messages = data.messages;
        this.totalMessages = data.totalMessages;
      },
      error: (errorResponse) => {
        this.messages = [];
        this.totalMessages = 0;
        this.handleError(errorResponse);
      }
    });
  }

  applyFilter(): void {
    const keyword = this.searchKeyword.trim();
    this.pageNo = 1; // نبدأ من أول صفحة
    this.loadMessages(keyword);
  }

  pageChange(page: number): void {
    this.pageNo = page;
    this.loadMessages();
  }

  sendReply(msg: ContactInfo): void {
    if (!msg.reply || msg.reply.trim() === '') {
      this.messageEn = 'Reply cannot be empty';
      this.messageAr = 'الرد لا يمكن أن يكون فارغًا';
      this.autoClearMessage();
      return;
    }

    this.contactService.updateMessage(msg).subscribe({
      next: (updated) => {
        this.messageEn = 'Reply sent successfully';
        this.messageAr = 'تم إرسال الرد بنجاح';
        msg.reply = updated.reply;
      },
      error: (errorResponse) => {
        this.handleError(errorResponse);
      }
    });
    this.autoClearMessage();
  }

  onSearch(): void {
    this.pageNo = 1;
    this.loadMessages();
  }

  autoClearMessage(): void {
    setTimeout(() => this.clearMessage(), 3000);
  }

  clearMessage(): void {
    this.messageEn = '';
    this.messageAr = '';
  }

  changePageSize = (event: Event) => {
    this.pageSize = +(event.target as HTMLInputElement).value;
    this.loadMessages();
  }


  saveChanges = (msg: ContactInfo) => {
    this.contactService.updateMessage(msg).subscribe({
      next: (res) => {
        this.messageEn = 'Message updated successfully';
        this.messageAr = 'تم تحديث الرسالة بنجاح';
        setTimeout(() => {
          this.messageEn = '';
          this.messageAr = '';
        }, 3000);
      },
      error: (errorResponse) => {
        this.handleError(errorResponse);
      }
    });
    this.autoClearMessage();
  }

  handleError = (errorResponse: any) => {
    if (Array.isArray(errorResponse.error) && errorResponse.error.length > 0) {
      this.messageEn = errorResponse.error[0].en;
      this.messageAr = errorResponse.error[0].ar;
    } else {
      this.messageEn = errorResponse.error.en;
      this.messageAr = errorResponse.error.ar;
    }
    this.autoClearMessage();
  }
}
