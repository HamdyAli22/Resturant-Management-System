import {ProductDetails} from './product-details';

export class Product {
  id: number;
  name: string;
  description: string;
  price: number;
  image: string;
  categoryId?: number;
  productDetailsDto?: ProductDetails;
}
