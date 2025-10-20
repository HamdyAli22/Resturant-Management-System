import {Component, OnInit} from '@angular/core';
import {CategoryService} from '../../../service/category.service';
import {Category} from '../../../model/category';
import {Router} from '@angular/router';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit{

  categories: Category[] = [];

  constructor(private categoryService: CategoryService,
              private router: Router) {

  }

  ngOnInit(): void {
       this.getCategories();
  }

  // tslint:disable-next-line:typedef
  getCategories(){
    this.categoryService.getCategories().subscribe(
      value => this.categories = value
    );
  }

  showCategories(): boolean {
    return this.router.url.startsWith('/products') || this.router.url.startsWith('/category');
  }
}
