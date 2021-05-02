import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { LoginService } from 'src/app/login/login.service';
import { UserService } from 'src/app/user/user.service';
import { User } from '../user.model';

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit {
  user = User.empty()
  errorMessage = ''
  loading = false
  userType = this.cookieService.get("user").toLowerCase()



  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    private loginService: LoginService,
    private cookieService: CookieService
  ) { }

  ngOnInit(): void {
    this.userService.userSelected.subscribe((user: User) => {
      this.user = user

      this.userService.errorChannel.subscribe(errorMessage => {
        this.errorMessage = errorMessage
      })
    })
    this.userService.getMyUser()
  }

  onEdit() {
    this.router.navigate(["edit", this.user.name, this.user.email], { relativeTo: this.route })
  }

  async onDelete() {
    this.errorMessage = ''
    const message = this.userType === 'admin' ?
      "are you sure you want to delete this admin?"
      :
      "are you sure you want to delete this user? user and all its coupons will be deleted!"
    if (confirm(message)) {

      if (prompt("please enter your password") === this.user.password) {
        this.loading = true
        this.userService.deleteUser(this.cookieService.get("token"), 0, this.user.id)
        await this.delay()
        this.loading = false
        if (this.errorMessage) {
          this.loginService.onLoginExpried(this.errorMessage)
          return
        }
        this.loginService.logout()
        this.router.navigate(["/login"])
      }
    }
  }

  private delay() {
    return new Promise<void>(resolve => {
      setTimeout(() => {
        resolve();
      }, 2000);
    })
  }
}
