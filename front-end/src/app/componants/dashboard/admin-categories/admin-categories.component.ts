import { Component, OnInit } from '@angular/core';
import {CategoryService} from '../../../../service/category.service';
import {Router} from '@angular/router';
import {Category} from '../../../../model/category';
import {ConfirmDialogComponent} from '../../confirm-dialog/confirm-dialog-component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AdminCategoryFormComponent} from './admin-category-form/admin-category-form.component';

@Component({
  selector: 'app-admin-categories',
  templateUrl: './admin-categories.component.html',
  styleUrls: ['./admin-categories.component.css']
})
export class AdminCategoriesComponent implements OnInit {

  categories: Category[] = [];
  pageNo = 1;
  pageSize = 10;
  totalCategories = 0;
  searchKey = '';
  messageAr = '';
  messageEn = '';

  constructor(private categoryService: CategoryService,
              private modalService: NgbModal) { }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.categoryService.getAllCategories(this.pageNo, this.pageSize).subscribe({
      next: (data) => {
        this.categories = data.categories;
        this.totalCategories = data.totalCategories;
      },
      error: (errorResponse) => {
        this.categories = [];
        this.handleError(errorResponse);
      }
    });
  }

  search(): void {
    if (!this.searchKey.trim()) {
      this.loadCategories();
      return;
    }
    this.categoryService.searchByKey(this.searchKey, this.pageNo, this.pageSize).subscribe({
      next: (res) => {
        this.categories = res.content || res.categories || [];
        this.totalCategories = res.totalElements || res.totalCategories || 0;
      },
      error: (errorResponse) => {
        this.categories = [];
        this.totalCategories = 0;
        this.handleError(errorResponse);
      }
    });
  }

  pageChange(page: number): void {
    this.pageNo = page;
    this.searchKey.trim() ? this.search() : this.loadCategories();
  }

  addCategory(): void {
    const modalRef = this.modalService.open(AdminCategoryFormComponent, { size: 'lg' });
    modalRef.componentInstance.mode = 'add';
    modalRef.result.then(() => this.loadCategories(), () => {});
  }

  editCategory(category: Category): void {
    const modalRef = this.modalService.open(AdminCategoryFormComponent, { size: 'lg' });
    modalRef.componentInstance.mode = 'edit';
    modalRef.componentInstance.category = { ...category };
    modalRef.result.then(() => this.loadCategories(), () => {});
  }

  deleteCategory(id: number): void {
    const modalRef = this.modalService.open(ConfirmDialogComponent, { centered: true });
    modalRef.componentInstance.title = 'Delete Category';
    modalRef.componentInstance.message = 'Are you sure you want to delete this category?';

    modalRef.result.then((result) => {
      if (result) {
        this.categoryService.deleteCategory(id).subscribe({
          next: () => {
            if (this.categories.length === 1 && this.pageNo > 1) {
              this.pageNo--;
            }
            this.loadCategories();
          },
          error: errorResponse => {
            this.handleError(errorResponse);
          }
        });
      }
    }).catch(() => {});
  }

  handleError = (errorResponse: any) => {
    if (Array.isArray(errorResponse.error) && errorResponse.error.length > 0) {
      this.messageEn = errorResponse.error[0].en;
      this.messageAr = errorResponse.error[0].ar;
    } else {
      this.messageEn = errorResponse.error.en;
      this.messageAr = errorResponse.error.ar;
    }
    this.autoClearMessage();
  }
  clearMessage(): void {
    this.messageEn = '';
    this.messageAr = '';
  }

  autoClearMessage(): void {
    setTimeout(() => this.clearMessage(), 5000);
  }
}
