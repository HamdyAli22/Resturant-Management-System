import {Component, HostListener, Input, OnInit} from '@angular/core';
import {Category} from '../../../../../model/category';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {CategoryService} from '../../../../../service/category.service';


@Component({
  selector: 'app-admin-category-form',
  templateUrl: './admin-category-form.component.html',
  styleUrls: ['./admin-category-form.component.css']
})
export class AdminCategoryFormComponent implements OnInit {

  @Input() mode: 'add' | 'edit' | 'view';
  @Input() category: Category = { id: null, name: '', logo: '', flag: '' };
  messageEn = '';
  messageAr = '';
  selectedIcon = '';
  showIconList = false;

  availableIcons: string[] = [
    'fa-utensils', 'fa-hamburger', 'fa-pizza-slice', 'fa-coffee', 'fa-ice-cream',
    'fa-drumstick-bite', 'fa-fish', 'fa-apple-alt', 'fa-bread-slice', 'fa-carrot',
    'fa-egg', 'fa-cookie', 'fa-wine-glass', 'fa-beer', 'fa-mug-hot', 'fa fa-birthday-cake',
    'fa-lemon', 'fa-pepper-hot', 'fa fa-shopping-cart', 'fa-hotdog'
  ];

  constructor(public activeModal: NgbActiveModal,
              private categoryService: CategoryService) { }

  ngOnInit(): void {
    this.selectedIcon = this.category.logo;
  }

  save(): void {
    const request = this.mode === 'add'
      ? this.categoryService.addCategory(this.category)
      : this.categoryService.updateCategory(this.category);

    request.subscribe({
      next: () => this.activeModal.close(),
      error: err => this.handleError(err)
    });
  }

  selectIcon(icon: string): void {
    this.selectedIcon = icon;
    this.category.logo = 'fa ' +icon;
    this.showIconList = false;
  }

  toggleIconList(): void {
    this.showIconList = !this.showIconList;
  }

  closeIconList(): void {
    this.showIconList = false;
  }

  @HostListener('document:keydown.escape', ['$event'])
  handleEscape(event: KeyboardEvent): void {
    this.showIconList = false;
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  handleError(errorResponse: any): void {
    if (Array.isArray(errorResponse.error)) {
      this.messageAr = errorResponse.error[0].ar;
      this.messageEn = errorResponse.error[0].en;
    } else {
      this.messageAr = errorResponse.error.ar;
      this.messageEn = errorResponse.error.en;
    }
    this.autoClearMessage();
  }

  autoClearMessage(): void {
    setTimeout(() => this.clearMessage(), 5000);
  }

  clearMessage(): void {
    this.messageEn = '';
    this.messageAr = '';
  }

  isReadOnly(): boolean {
    return this.mode === 'view';
  }
}
