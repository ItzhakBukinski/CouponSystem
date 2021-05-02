import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { LoginService } from 'src/app/login/login.service';
import { User } from 'src/app/user/user.model';
import { UserService } from 'src/app/user/user.service';
import { Coupon } from '../coupon.model';
import { CouponService } from '../coupon.service';

@Component({
  selector: 'app-coupon-details',
  templateUrl: './coupon-details.component.html',
  styleUrls: ['./coupon-details.component.css']
})
export class CouponDetailsComponent implements OnInit {
  user: User
  coupon: Coupon
  id: number
  customerLogged = this.cookieService.check("user") && this.cookieService.get("user") === "CUSTOMER"
  errorMessage = ''
  loading = false
  constructor(
    private couponService: CouponService,
    private router: Router,
    private route: ActivatedRoute,
    private cookieService: CookieService,
    private loginService: LoginService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.userService.userSelected.subscribe((user: User) => {
      this.user = user
    })

    this.route.params.subscribe((params: Params) => {
      this.id = params['id']
      this.coupon = this.couponService.getCouponById(this.id)
    })

    this.couponService.errorChannel.subscribe(errorMessage => {
      this.errorMessage = errorMessage
    })
    this.userService.errorChannel.subscribe(errorMessage => {
      this.errorMessage = errorMessage
    })
  }

  async onBuy() {
    this.errorMessage = ''
    if (confirm("are you sure you want to buy this coupon?")) {
      this.loading = true
      this.userService.getMyUser()
      await this.delay()
      if (this.errorMessage) {
        this.loginService.onLoginExpried(this.errorMessage)
        this.loading = false
        return
      }
      if (prompt("please enter your password") === this.user.password) {
        this.couponService.purchaseCoupon(this.cookieService.get("token"), this.coupon.id)
        await this.delay()
        this.loading = false
        if (this.errorMessage) {
          this.loginService.onLoginExpried(this.errorMessage)
        }
        else {
          this.router.navigate(["/user"])
        }
      }
      else {
        this.loading = false
        return
      }
    }
  }
  onLogout() {
    this.loginService.logout()
    this.router.navigate(["/login"])
  }

  checkEndDate() {
    const couponEndDate = new Date(this.coupon.endDate)
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
