// @ts-ignore

import { Component, OnInit } from '@angular/core';
import {Product} from '../../../../model/product';
import {ProductService} from '../../../../service/product.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AdminProductFormComponent} from './admin-product-form/admin-product-form.component';
import {ConfirmDialogComponent} from '../../confirm-dialog/confirm-dialog-component';
import {CategoryService} from '../../../../service/category.service';
import {Category} from '../../../../model/category';

@Component({
  selector: 'app-admin-products',
  templateUrl: './admin-products.component.html',
  styleUrls: ['./admin-products.component.css']
})
export class AdminProductsComponent implements OnInit {

  products: Product[] = [];
  categories: Category[] = [];
  pageNo = 1;
  pageSize = 10;
  totalProducts = 0;
  searchKey = '';
  messageAr = '';
  messageEn = '';

  constructor(private productService: ProductService,
              private modalService: NgbModal,
              private categoryService: CategoryService) { }

  ngOnInit(): void {
    this.loadProducts();
    this.loadCategories();
  }

  loadCategories(): void {
    this.categoryService.getCategories().subscribe({
      next: (data) => {
        this.categories = data;
      }
    });
  }

  loadProducts(): void {
    this.productService.getProducts(this.pageNo, this.pageSize).subscribe(res => {
      this.products = res.products;
      this.totalProducts = res.totalProducts;
    }, errorResponse => {
      this.products = [];
      this.messageEn = errorResponse.error.en;
      this.messageAr = errorResponse.error.ar;
    });
  }

  search(): void {
    if (!this.searchKey.trim()) {
      this.loadProducts();
      return;
    }
    this.productService.searchByKey(this.searchKey, this.pageNo, this.pageSize).subscribe(res => {
      this.products = res.products;
      this.totalProducts = res.totalProducts;
    }, errorResponse => {
      this.products = [];
      this.messageEn = errorResponse.error.en;
      this.messageAr = errorResponse.error.ar;
      this.totalProducts = 0;
    });
  }

  pageChange(page: number): void {
    this.pageNo = page;
    this.loadProducts();
  }

  addProduct(): void {
     const modalRef = this.modalService.open(AdminProductFormComponent, { size: 'lg' });
     modalRef.componentInstance.mode = 'add';
     modalRef.result.then(() => this.loadProducts(), () => {});
  }

  editProduct(product: Product): void {
     const modalRef = this.modalService.open(AdminProductFormComponent, { size: 'lg' });
     modalRef.componentInstance.mode = 'edit';
     modalRef.componentInstance.product = { ...product };
     modalRef.result.then(() => this.loadProducts(), () => {});
  }

  deleteProduct(id: number): void {
    const modalRef = this.modalService.open(ConfirmDialogComponent, { centered: true });
    modalRef.componentInstance.title = 'Delete Product';
    modalRef.componentInstance.message = 'Are you sure you want to delete this product?';

    modalRef.result.then((result) => {
      if (result) {
        this.productService.deleteProduct(id).subscribe({
          next: () => {
            if (this.products.length === 1 && this.pageNo > 1) {
              this.pageNo--;
            }
            this.loadProducts();
          },
          error: errorResponse => {
            if (Array.isArray(errorResponse.error)) {
              this.messageAr = errorResponse.error.map((e: any) => e.ar).join('، ');
              this.messageEn = errorResponse.error.map((e: any) => e.en).join(', ');
            } else {
              this.messageAr = errorResponse.error.ar;
              this.messageEn = errorResponse.error.en;
            }
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
  getCategoryName(categoryId: number): string {
    const category = this.categories.find(c => c.id === categoryId);
    return category ? category.name : '';
  }

}
