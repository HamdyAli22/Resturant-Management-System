import {Component, OnInit} from '@angular/core';
import {Router, Routes} from '@angular/router';
import {CardService} from '../../../service/card.service';
import {ProductOrder} from '../../../model/product-order';
import {Product} from '../../../model/product';
import {RequestOrderService} from '../../../service/request-order.service';

@Component({
  selector: 'app-card-details',
  templateUrl: './card-details.component.html',
  styleUrls: ['./card-details.component.css']
})
export class CardDetailsComponent implements OnInit {
  productOrders: ProductOrder[] = [];
  totalProductSize = 0 ;
  totalProductPrice = 0;
  messageAr = '';
  messageEn = '';
  routes: Routes = [];

  constructor(private cardService: CardService, private orderService: RequestOrderService,
              private router: Router) {
  }

  getCardOrderDetails = () => {
      this.productOrders = this.cardService.productOrders;
      this.cardService.totalSize.subscribe(value => {this.totalProductSize = value; });
      this.cardService.totalPrice.subscribe(value => {this.totalProductPrice = value; });
  }

  ngOnInit(): void {
   this.getCardOrderDetails();
  }

  addProduct = (productOrder: ProductOrder) => {
    this.cardService.addProductToOrder(productOrder);
  }

  removeProduct = (productOrder: ProductOrder) => {
    this.cardService.removeProduct(productOrder);
  }

  removeFullProduct = (productOrder: ProductOrder) => {
    this.cardService.remove(productOrder);
  }

  createOrder = () => {
    const productIds = this.productOrders.map(item => item.id);
    this.orderService.createOrder(productIds, this.totalProductPrice, this.totalProductSize)
      .subscribe({
        next: (response: any) => {
          this.cardService.productOrders = [];
          this.cardService.totalSize.next(0);
          this.cardService.totalPrice.next(0);
          this.router.navigateByUrl('/order-code/' + response.code);
        },
        error: (errorResponse) => {
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
    setTimeout(() => this.clearMessage(), 5000);
  }
}
