import { Component, OnInit } from '@angular/core';
import {ChefsService} from '../../../service/chefs.service';
import {Chefs} from '../../../model/chefs';

@Component({
  selector: 'app-chefs',
  templateUrl: './chefs.component.html',
  styleUrls: ['./chefs.component.css']
})
export class ChefsComponent implements OnInit {

  chefs: Chefs[] = [];

  constructor(private chefService: ChefsService) { }

  ngOnInit(): void {
    this.getChefs();
  }

  // tslint:disable-next-line:typedef
   getChefs() {
    this.chefService.getChefs().subscribe(
      value => this.chefs = value
    );
  }
}
