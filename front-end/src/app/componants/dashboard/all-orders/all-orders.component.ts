import { Component, OnInit } from '@angular/core';
import {OrderService} from '../../../../service/order.service';
import { Order } from '../../../../model/order';
import {OrdersResponse} from '../../../../model/user-order-response';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ConfirmDialogComponent} from '../../confirm-dialog/confirm-dialog-component';

@Component({
  selector: 'app-all-orders',
  templateUrl: './all-orders.component.html',
  styleUrls: ['./all-orders.component.css']
})
export class AllOrdersComponent implements OnInit {

  orders: Order[] = [];
  pageNo = 1;
  pageSize = 10;
  totalOrders: number;
  size = 0;
  price = 0;
  messageAr = '';
  messageEn = '';
  filterUsername = '';
  selectedOrder: Order | null = null;

  constructor(private orderService: OrderService, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.loadAllOrders();
  }

  loadAllOrders(username?: string): void {
    this.orderService.getAllOrders(this.pageNo, this.pageSize, username).subscribe(
      (response: OrdersResponse) => {
        this.orders = response.orders.map(order => ({
          ...order,
          orderDate: new Date(order.orderDate)
        }));

        this.totalOrders = response.totalOrders;
        this.size = response.size;
        this.price = response.price;
      },
      errorResponse => {
        this.orders = [];
        this.messageEn = errorResponse.error.en;
        this.messageAr = errorResponse.error.ar;
        this.totalOrders = 0;
        this.size = 0;
        this.price = 0;
      }
    );
  }

  pageChange(page: number): void {
    this.pageNo = page;
    this.loadAllOrders(this.filterUsername.trim());
  }

  applyFilter(): void {
    const keyword = this.filterUsername.trim();
    this.pageNo = 1;
    this.loadAllOrders(keyword);
  }

  viewDetails(order: Order): void {
    this.orderService.getOrderById(order.id).subscribe(
      data => {
        this.selectedOrder = data;
      },
      errorResponse => {
        this.messageEn = errorResponse.error.en;
        this.messageAr = errorResponse.error.ar;
      }
    );
  }

  closeDetails(): void {
    this.selectedOrder = null;
  }

  deleteOrder(id: number): void {
    const modalRef = this.modalService.open(ConfirmDialogComponent, { centered: true });
    modalRef.componentInstance.title = 'Delete Order';
    modalRef.componentInstance.message = 'Are you sure you want to delete this order?';

    modalRef.result.then((result) => {
      if (result) {
        this.orderService.deleteOrder(id).subscribe({
          next: () => {
            if (this.orders.length === 1 && this.pageNo > 1) {
              this.pageNo--;
            }
            this.loadAllOrders();
          },
          error: errorResponse => {
            this.messageEn = errorResponse.error.en;
            this.messageAr = errorResponse.error.ar;
            this.autoClearMessage();
          }
        });
      }
    }).catch(() => {});
  }

  clearMessage(): void {
    this.messageEn = '';
    this.messageAr = '';
  }

  autoClearMessage(): void {
    setTimeout(() => this.clearMessage(), 5000);
  }
}
