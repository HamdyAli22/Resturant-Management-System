import {Component, OnInit} from '@angular/core';
import {Product} from '../../../model/product';
import {ProductService} from '../../../service/product.service';
import {ActivatedRoute} from '@angular/router';
import {CardService} from '../../../service/card.service';
import {ProductOrder} from '../../../model/product-order';
import {AdminProductFormComponent} from '../dashboard/admin-products/admin-product-form/admin-product-form.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent  implements OnInit{

  products: Product[] = [];
  pageNo = 1;
  pageSize = 10;
  totalProducts: number;
  messageAr = '';
  messageEn = '';

  constructor(private productService: ProductService, private activeRoute: ActivatedRoute,
              private cardService: CardService,
              private modalService: NgbModal) {

  }

  ngOnInit(): void {
    this.activeRoute.paramMap.subscribe(params => {this.loadProducts(this.pageNo); });
  }

  loadProducts(pageNo): void {
    const hasCategoryId = this.activeRoute.snapshot.paramMap.has('id');
    const hasKey = this.activeRoute.snapshot.paramMap.has('key');
    if (hasCategoryId) {
      const categoryId = this.activeRoute.snapshot.paramMap.get('id');
      this.getProductsByCatrgoryId(categoryId, pageNo);
      return;
    }else if (hasKey) {
      const key = this.activeRoute.snapshot.paramMap.get('key');
      this.searchByKey(key, pageNo);
      return;
    }
    this.getProducts(pageNo);
  }

  getProducts = (pageNo) => {
    this.productService.getProducts(pageNo, this.pageSize).subscribe(
      response => {
        this.products = response.products;
        this.totalProducts = response.totalProducts;
      }, errorResponse => {
        this.products = [];
        this.messageEn = errorResponse.error.en;
        this.messageAr = errorResponse.error.ar;
      }
    );
  }

  // tslint:disable-next-line:typedef
  private getProductsByCatrgoryId(id, pageNo) {
    this.productService.getProductsByCategoryId(id, pageNo, this.pageSize).subscribe(
      response => {
        this.products = response.products;
        this.totalProducts = response.totalProducts;
      }, errorResponse => {
        this.products = [];
        this.totalProducts = 0;
        this.messageEn = errorResponse.error.en;
        this.messageAr = errorResponse.error.ar;
      }
    );
  }

  // tslint:disable-next-line:typedef
  private searchByKey(key, pageNo) {
    this.productService.searchByKey(key, pageNo , this.pageSize).subscribe(
      response => {
        this.products = response.products;
        this.totalProducts = response.totalProducts;
      }, errorResponse => {
      this.products = [];
      this.totalProducts = 0;
      this.messageEn = errorResponse.error.en;
      this.messageAr = errorResponse.error.ar;
    }
    );
  }

doPagination = () => {
    this.loadProducts(this.pageNo);
}

changePageSize = (event: Event) => {
  this.pageSize = +(event.target as HTMLInputElement).value;
  this.loadProducts(this.pageNo);
}

  addProduct = (product: Product) => {
    const productOrder = new ProductOrder(product);
    this.cardService.addProductToOrder(productOrder);
  }

  showDetails = (pro: Product) => {
    const modalRef = this.modalService.open(AdminProductFormComponent, { size: 'lg' });
    modalRef.componentInstance.mode = 'view';
    modalRef.componentInstance.product = pro;
  }

}
