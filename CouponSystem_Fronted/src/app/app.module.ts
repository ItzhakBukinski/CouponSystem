import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http'
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { CouponsComponent } from './coupons/coupons.component';
import { FormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';
import { CouponsListComponent } from './coupons/coupons-list/coupons-list.component';
import { CouponItemComponent } from './coupons/coupons-list/coupon-item/coupon-item.component';
import { CouponDetailsComponent } from './coupons/coupon-details/coupon-details.component';
import { StorageService } from './common/storage.service';
import { CouponService } from './coupons/coupon.service';
import { LoginService } from './login/login.service';
import { UserComponent } from './user/user.component';
import { CookieService } from 'ngx-cookie-service';
import { SignUpComponent } from './login/sign-up/sign-up.component';
import { UserDetailComponent } from './user/user-detail/user-detail.component';
import { UserEditComponent } from './user/user-edit/user-edit.component';
import { UserService } from './user/user.service';
import { CouponEditComponent } from './coupons/coupon-edit/coupon-edit.component';
import { UsersListComponent } from './user/users-list/users-list.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    CouponsComponent,
    LoginComponent,
    CouponsListComponent,
    CouponItemComponent,
    CouponDetailsComponent,
    UserComponent,
    SignUpComponent,
    UserDetailComponent,
    UserEditComponent,
    CouponEditComponent,
    UsersListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [
    StorageService,
    CouponService,
    LoginService,
    CookieService,
    UserService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
