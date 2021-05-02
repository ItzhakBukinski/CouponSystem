import { Component, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Coupon } from '../../coupon.model';

@Component({
  selector: 'app-coupon-item',
  templateUrl: './coupon-item.component.html',
  styleUrls: ['./coupon-item.component.css'],
})

export class CouponItemComponent {
  @Input() coupon: Coupon
  @Input() id: number

  constructor(private router: Router, private route: ActivatedRoute) { }

  onClick() {
    this.router.navigate(['details', this.id], { relativeTo: this.route })
  }

  checkEndDate() {
    const couponEndDate = new Date(this.coupon.endDate)
    let inAWeek = new Date()
    inAWeek.setDate(inAWeek.getDate() + 7)
    return inAWeek.getTime() > couponEndDate.getTime() ? 'red' : 'darkblue'
  }
}