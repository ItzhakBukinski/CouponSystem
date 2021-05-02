import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { LoginService } from 'src/app/login/login.service';
import { Coupon } from '../coupon.model';
import { CouponService } from '../coupon.service';

@Component({
  selector: 'app-coupons-list',
  templateUrl: './coupons-list.component.html',
  styleUrls: ['./coupons-list.component.css']
})

export class CouponsListComponent implements OnInit {
  coupons: Coupon[]
  coupon: Coupon
  errorMessage = ''
  logged = this.router.url.includes("customer")
  noCoupons: boolean
  price: number
  date: string
  @ViewChild("p") priceInput: NgForm
  @ViewChild("d") dateInput: NgForm

  constructor(
    private couponService: CouponService,
    private cookieService: CookieService,
    private router: Router,
    private loginService: LoginService
  ) { }

  async ngOnInit(): Promise<void> {
    this.couponService.couponsChanged.subscribe((coupons: Coupon[]) => {
      this.coupons = coupons
      this.noCoupons = this.coupons.length === 0
    })

    this.couponService.errorChannel.subscribe((errorMessage: string) => {
      this.errorMessage = errorMessage
    })

    if (this.logged) {
      this.couponService.fetchNotPurchasedCoupons(this.cookieService.get("token"))
      await this.delay()
      if (this.errorMessage) {
        this.loginService.onLoginExpried(this.errorMessage)
      }
    }
    else {
      this.couponService.fetchAllCoupons(undefined, undefined)
    }
  }

  async fetchByCategory(category: string) {
    if (this.logged) {
      this.couponService.fetchAllCouponsByCategory(this.cookieService.get("token"), category)
      await this.delay()
      if (this.errorMessage) {
        this.loginService.onLoginExpried(this.errorMessage)
      }
    }
    else {
      this.couponService.fetchAllCouponsByCategory(undefined, category)
    }
  }

  async fetchByPrice() {
    if (this.logged) {
      this.couponService.fetchAllCouponsLessThanPrice(this.cookieService.get("token"), this.price)
      await this.delay()
      if (this.errorMessage) {
        this.loginService.onLoginExpried(this.errorMessage)
      }
    }
    else {
      this.couponService.fetchAllCouponsLessThanPrice(undefined, this.price)
    }
    this.priceInput.reset()
  }

  async fetchByDate() {
    if (this.logged) {
      this.couponService.fetchAllCouponsBeforeDate(this.cookieService.get("token"), this.date)
      await this.delay()
      if (this.errorMessage) {
        this.loginService.onLoginExpried(this.errorMessage)
      }
    }
    else {
      this.couponService.fetchAllCouponsBeforeDate(undefined, this.date)
    }
    this.dateInput.reset()
  }

  private delay() {
    return new Promise<void>(resolve => {
      setTimeout(() => {
        resolve();
      }, 2000);
    })
  }
}
