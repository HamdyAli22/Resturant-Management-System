import { Component, OnInit } from '@angular/core';
import {OrderService} from '../../../../service/order.service';
import { Order } from '../../../../model/order';

@Component({
  selector: 'app-orders-history',
  templateUrl: './orders-history.component.html',
  styleUrls: ['./orders-history.component.css']
})
export class OrdersHistoryComponent implements OnInit {

  orders: Order[] = [];
  size = 0;
  price = 0;
  messageAr = '';
  messageEn = '';

  constructor(private orderService: OrderService) { }

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.orderService.getUserOrders().subscribe(
      response => {
        this.orders = response.orders;
        this.size = response.size;
        this.price = response.price;
      },
       errorResponse => {
        this.orders = [];
        this.messageEn = errorResponse.error.en;
        this.messageAr = errorResponse.error.ar;
        this.size = 0;
        this.price = 0;
      }
    );
  }
}
