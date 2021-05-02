import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserDetailComponent } from './user/user-detail/user-detail.component';
import { UserEditComponent } from './user/user-edit/user-edit.component';
import { CouponDetailsComponent } from './coupons/coupon-details/coupon-details.component';
import { CouponsListComponent } from './coupons/coupons-list/coupons-list.component';
import { CouponsComponent } from './coupons/coupons.component';
import { LoginComponent } from './login/login.component';
import { SignUpComponent } from './login/sign-up/sign-up.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { UserComponent } from './user/user.component';
import { CouponEditComponent } from './coupons/coupon-edit/coupon-edit.component';
import { UsersListComponent } from './user/users-list/users-list.component';

const routes: Routes = [

  /* Homepage: */
  { path: "", redirectTo: "/coupons", pathMatch: "full" },

  /* Coupons- open: */
  {
    path: "coupons", component: CouponsComponent, children: [
      { path: "", component: CouponsListComponent },
      { path: "details/:id", component: CouponDetailsComponent }
    ]
  },
  /* Customer's unpurchaced coupons:  */
  {
    path: "customer-new-coupons", component: CouponsComponent, children: [
      { path: "", component: CouponsListComponent },
      { path: "details/:id", component: CouponDetailsComponent }
    ]
  },

  /* Login and sign-up: */
  { path: "login", component: LoginComponent },
  { path: "login/:error", component: LoginComponent },
  { path: "sign-up", component: SignUpComponent },

  /* User: */
  {
    path: "user", component: UserComponent, children: [
      { path: "", component: UserDetailComponent },
      { path: "edit/:name/:email", component: UserEditComponent },

      /* Company adding/updating coupon: */
      { path: "new", component: CouponEditComponent },
      { path: ":id/edit", component: CouponEditComponent }
    ]
  },

  /* List of all users: */
  { path: "users", component: UsersListComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes,
    {
      scrollPositionRestoration: "enabled",
      scrollOffset: [0, 0],
      anchorScrolling: "enabled"
    })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
