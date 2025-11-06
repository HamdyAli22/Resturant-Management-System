import {Component, Input, OnInit} from '@angular/core';
import {Product} from '../../../../../model/product';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {ProductService} from '../../../../../service/product.service';
import {Category} from '../../../../../model/category';
import {CategoryService} from '../../../../../service/category.service';
import {FileUploadService} from '../../../../../service/fileUpload.service';
import {ProductOrder} from '../../../../../model/product-order';
import {CardService} from '../../../../../service/card.service';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-admin-product-form',
  templateUrl: './admin-product-form.component.html',
  styleUrls: ['./admin-product-form.component.css']
})
export class AdminProductFormComponent implements OnInit {

  @Input() mode: 'add' | 'edit' | 'view';
  @Input() product: Product = {
    id: null,
    name: null,
    description: null,
    price: null,
    image: '',
    categoryId: null,
    productDetailsDto: {
      ingredients: null,
      stockAvailability: true,
      specialInstructions: null,
      expiryDate: ''
    }
  };
  categories: Category[] = [];
  messageAr = '';
  messageEn = '';
  todayDate = '';
  selectedFolder = 'img';
  selectedFile!: File;
  constructor(
              public activeModal: NgbActiveModal,
              private productService: ProductService,
              private categoryService: CategoryService,
              private uploadService: FileUploadService,
              private cardService: CardService,
              private cdr: ChangeDetectorRef
             ){}

  ngOnInit(): void {
    this.loadCategories();

    if (!this.product.productDetailsDto) {
      this.product.productDetailsDto = {
        id: null,
        ingredients: '',
        stockAvailability: false,
        specialInstructions: '',
        expiryDate: ''
      };
    }

    if (this.mode === 'edit' && this.product.image) {
      const parts = this.product.image.split('/');
      if (parts.length > 1) {
        this.selectedFolder = parts[0]; // أول جزء من المسار هو الفولدر
      }
    }

    const today = new Date();
    this.todayDate = today.toISOString().split('T')[0];
  }

  loadCategories(): void {
    this.categoryService.getCategories().subscribe({
      next: (data) => {
        this.categories = data;
      }
    });
  }

  save(event?: Event): void {
    event?.preventDefault();
    event?.stopPropagation();
    if (this.mode === 'add') {
      this.productService.addProduct(this.product).subscribe({
        next: () => {
          this.activeModal.close();
          },
        error: errorResponse =>  {
          if (Array.isArray(errorResponse.error)) {
            this.messageAr = errorResponse.error[0].ar;
            this.messageEn = errorResponse.error[0].en;
          } else {
            this.messageAr = errorResponse.error.ar;
            this.messageEn = errorResponse.error.en;
          }
          this.autoClearMessage();
        }
      }
      );
    }else if (this.mode === 'edit') {
      this.productService.updateProduct(this.product).subscribe({
        next: () => {
          this.activeModal.close();
        },
        error: errorResponse => {
          if (Array.isArray(errorResponse.error)) {
            this.messageAr = errorResponse.error[0].ar;
            this.messageEn = errorResponse.error[0].en;
          } else {
            this.messageAr = errorResponse.error.ar;
            this.messageEn = errorResponse.error.en;
          }
          this.autoClearMessage();
        }
      });
    }



  }

  autoClearMessage(): void {
    setTimeout(() => this.clearMessage(), 5000);
  }

  clearMessage(): void {
    this.messageEn = '';
    this.messageAr = '';
  }

  onFileSelected = (event: any) => {
   // event.preventDefault();
    event.stopPropagation();
    this.selectedFile = event.target.files[0];

    if (this.selectedFile) {
      this.uploadService.uploadFile(this.selectedFile, this.selectedFolder).subscribe({
        next: (res) => {
          this.product.image = res.path; // من JSON
          console.log('✅ File uploaded, saved path:', this.product.image);
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('❌ Upload failed', err);
          this.messageEn = 'Image upload failed';
        }
      });
    }
  }

  isReadOnly(): boolean {
    return this.mode === 'view';
  }

  addToCard = (product: Product) => {
    const productOrder = new ProductOrder(product);
    this.cardService.addProductToOrder(productOrder);
    this.activeModal.close();
  }
}
