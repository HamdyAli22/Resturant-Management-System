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
  pageNo = 1;
  pageSize = 10;
  totalOrders: number;
  messageAr = '';
  messageEn = '';
  searchKeyword = '';
  constructor(private orderService: OrderService) { }

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(keyword?: string): void {
    const apiCall = keyword
      ? this.orderService.searchUserOrders(keyword, this.pageNo, this.pageSize)
      : this.orderService.getUserOrders(this.pageNo, this.pageSize);

    apiCall.subscribe(
      response => {
        this.orders = response.orders;
        this.totalOrders = response.totalOrders;
        this.size = response.size;
        this.price = response.price;
        this.messageAr = '';
        this.messageEn = '';
      },
      errorResponse => {
        this.orders = [];
        this.messageEn = errorResponse.error.en || 'An error occurred';
        this.messageAr = errorResponse.error.ar || 'حدث خطأ ما';
        this.totalOrders = 0;
        this.size = 0;
        this.price = 0;
      }
    );
  }

  pageChange(page: number): void {
    this.pageNo = page;
    this.loadOrders();
  }

  changePageSize = (event: Event) => {
    this.pageSize = +(event.target as HTMLInputElement).value;
    this.loadOrders();
  }

  applyFilter(): void {
    const keyword = this.searchKeyword.trim();
    this.pageNo = 1; // نبدأ من أول صفحة
    this.loadOrders(keyword);
  }
}
