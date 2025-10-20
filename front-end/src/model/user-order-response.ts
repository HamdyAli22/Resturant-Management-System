import {Order} from './order';

export interface OrdersResponse {
  orders: Order[];
  size: number;
  price: number;
  totalOrders: number;
}
