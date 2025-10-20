import { Injectable } from '@angular/core';
import {ProductOrder} from '../model/product-order';
import {BehaviorSubject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CardService {

  productOrders: ProductOrder[] = [];
  totalPrice = new BehaviorSubject<number>(0);
  totalSize = new BehaviorSubject<number>(0);

  constructor() { }

  addProductToOrder = (product: ProductOrder) => {
    let isExist = false;
    let existedProduct: ProductOrder;

    if (this.productOrders.length > 0){
      existedProduct = this.productOrders.find(productOrder => productOrder.id === product.id);
    }
    isExist = (existedProduct !== undefined);
    if (isExist){
      existedProduct.quantity ++;
    }else {
      this.productOrders.push(product);
    }
    this.calculateTotals();
  }

  removeProduct = (product: ProductOrder) => {
    product.quantity --;
    if (product.quantity === 0){
       this.remove(product);
    }
    this.calculateTotals();
  }

  remove = (product: ProductOrder) => {
    const index = this.productOrders.findIndex(productOrder => productOrder.id === product.id);
    if (index > -1){
      this.productOrders.splice(index, 1);
    }
  }

  calculateTotals = () => {
    let totalElementPrice = 0;
    let totalElemntSize = 0;

    for (const order of this.productOrders){
      totalElementPrice += order.quantity * order.price;
      totalElemntSize +=   order.quantity;
    }
    this.totalPrice.next(totalElementPrice);
    this.totalSize.next(totalElemntSize);
  }
}
