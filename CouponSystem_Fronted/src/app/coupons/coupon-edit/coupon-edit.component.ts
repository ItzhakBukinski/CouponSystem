import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { Coupon } from 'src/app/coupons/coupon.model';
import { CouponService } from 'src/app/coupons/coupon.service';
import { LoginService } from 'src/app/login/login.service';


@Component({
  selector: 'app-coupon-edit',
  templateUrl: './coupon-edit.component.html',
  styleUrls: ['./coupon-edit.component.css']
})
export class CouponEditComponent implements OnInit {
  coupon = Coupon.empty()
  @ViewChild("f") contactForm: NgForm

  errorMessage = ''
  id: number
  editMode: boolean
  loading = false

  constructor(
    private couponService: CouponService,
    private route: ActivatedRoute,
    private router: Router,
    private cookieService: CookieService,
    private loginService: LoginService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => {
      this.id = params["id"]
      this.editMode = this.id != null

      if (this.editMode) {
        this.coupon = this.couponService.getCouponById(this.id)
      }
    })
  }

  async onSubmit() {
    this.couponService.errorChannel.next('')
    this.loading = true

    this.couponService.errorChannel.subscribe(errorMessage => {
      this.errorMessage = errorMessage
    })

    if (this.editMode)
      this.couponService.updateCoupon(this.cookieService.get("token"), this.coupon, this.id)

    else
      this.couponService.appendCoupon(this.cookieService.get("token"), this.coupon)

    await this.delay()
    this.loading = false

    if (!this.errorMessage) {
      this.router.navigate(["/user"])
    }
    else {
      this.loginService.onLoginExpried(this.errorMessage)
    }
  }

  onClear() {
    this.coupon = Coupon.empty()
    this.couponService.errorChannel.next('')
    this.contactForm.reset()
    this.couponService.errorChannel.next('')
  }

  private delay() {
    return new Promise<void>(resolve => {
      setTimeout(() => {
        resolve();
      }, 2000);
    })
  }

}
