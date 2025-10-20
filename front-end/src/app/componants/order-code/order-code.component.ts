import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-order-code',
  templateUrl: './order-code.component.html',
  styleUrls: ['./order-code.component.css']
})
export class OrderCodeComponent implements OnInit {

   orderCode = '';
  showOrderCode = true;

  constructor(private activatedRoute: ActivatedRoute, private router: Router ) { }

  ngOnInit(): void {
    const hasCode = this.activatedRoute.snapshot.paramMap.has('code');
    if (hasCode) {
      this.orderCode = this.activatedRoute.snapshot.paramMap.get('code');
    }else{
      this.orderCode = 'No code found,Please create order';
    }
  }

  goToProducts = () => {
    this.showOrderCode = false;
    this.router.navigate(['/products']);
  }


}
