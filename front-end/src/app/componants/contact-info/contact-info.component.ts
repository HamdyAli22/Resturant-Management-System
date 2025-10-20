import { Component, OnInit } from '@angular/core';
import {ContactInfo} from '../../../model/contact-info';
import {ContactInfoService} from '../../../service/contact-info.service';

@Component({
  selector: 'app-contact-info',
  templateUrl: './contact-info.component.html',
  styleUrls: ['./contact-info.component.css']
})
export class ContactInfoComponent implements OnInit {

  constructor(private contactService: ContactInfoService) { }

  contact: ContactInfo = {
    name: null,
    email: null,
    subject: null,
    message: null
  };
  messageAr = '';
  messageEn = '';
  ngOnInit(): void {
  }

  saveContact(): void {
    this.contactService.saveContact(this.contact).subscribe({
      next: (response: any) => {
        this.messageAr = 'تم إرسال الرسالة بنجاح.';
        this.messageEn = 'Message sent successfully.';
        this.contact = new ContactInfo();
      },
      error: errorResponse => {
        this.handleError(errorResponse);
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
