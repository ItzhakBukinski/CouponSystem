import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { Coupon } from '../coupons/coupon.model';
import { CouponService } from '../coupons/coupon.service';
import { LoginService } from '../login/login.service';
import { User } from './user.model';
import { UserService } from './user.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  coupons: Coupon[]
  coupon: Coupon
  user = User.empty()
  sortType = 0
  errorMessage = ''
  loading = false
  userType = this.cookieService.get("user").toLowerCase()

  constructor(
    private loginService: LoginService,
    private cookieService: CookieService,
    private couponService: CouponService,
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  async ngOnInit(): Promise<void> {
    this.couponService.errorChannel.subscribe(errorMessage => {
      this.errorMessage = errorMessage
    })
    this.userService.userSelected.subscribe((user: User) => {
      this.user = user
    })

    this.couponService.couponsChanged.subscribe((coupons: Coupon[]) => {
      this.coupons = coupons
    })

    this.couponService.fetchAllCoupons(this.cookieService.get("token"), this.sortType)
    await this.delay()
    if (this.errorMessage) {
      this.loginService.onLoginExpried(this.errorMessage)
    }
  }

  onEditClicked(id: number) {
    this.coupon = this.couponService.getCouponById(id)
    this.couponService.couponSelected.emit(this.coupon)
    this.router.navigate([id + "/edit"], { relativeTo: this.route })
  }

  async onDelete(id: number) {
    this.errorMessage = ''
    this.loading = true
    await this.delay()
    this.loading = false
    if (confirm("are you sure you want to delete this coupon?")) {
      if (prompt("please enter your password") === this.user.password) {
        this.loading = true
        this.couponService.deleteCoupon(this.cookieService.get("token"), id, this.couponService.getCouponById(id))
        await this.delay()
        this.loading = false
        if (this.errorMessage) {
          this.loginService.onLoginExpried(this.errorMessage)
        }
        this.router.navigate(["/user"])
      }
    }
  }

  async onDeleteAllCoupons() {
    this.errorMessage = ''

    if (confirm("are you sure you want to delete all coupons??")) {
      if (prompt("please enter your password") === this.user.password) {
        this.loading = true
        this.couponService.deleteAllCoupons(this.cookieService.get("token"))
        await this.delay()
        this.loading = false

        if (this.errorMessage) {
          this.loginService.onLoginExpried(this.errorMessage)
        }
        this.router.navigate(["/user"])
      }
    }
  }

  sortBy(sortType: number) {
    this.sortType = sortType
    this.ngOnInit()
  }

  checkEndDate(coupon: Coupon) {
    const couponEndDate = new Date(coupon.endDate)
    let inAWeek = new Date()
    inAWeek.setDate(inAWeek.getDate() + 7)
    return inAWeek.getTime() > couponEndDate.getTime() ? 'red' : ''
  }

  private delay() {
    return new Promise<void>(resolve => {
      setTimeout(() => {
        resolve();
      }, 2000);
    })
  }
}
