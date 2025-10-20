import {Product} from './product';
import {Account} from './account';

export class Order {
  id: number;
  code: string;
  totalPrice: number;
  totalNumber: number;
  orderDate: string| Date;
  products: Product[];
  account?: Account;
}
