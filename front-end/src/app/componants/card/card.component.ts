import {Component, OnInit} from '@angular/core';
import {Routes} from '@angular/router';
import {CardService} from '../../../service/card.service';


@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})

export class CardComponent implements OnInit {
  totalProductSize = 0 ;
  totalProductPrice = 0;
  routes: Routes = [];

  constructor(private cardService: CardService) {
  }

  ngOnInit(): void {
        this.getTotals();
    }

  getTotals = () => {
    this.cardService.totalPrice.subscribe((value) => {this.totalProductPrice = value; });
    this.cardService.totalSize.subscribe((value) => {this.totalProductSize = value; });
  }
}
