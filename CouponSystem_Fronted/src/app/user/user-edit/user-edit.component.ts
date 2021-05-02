import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { LoginService } from 'src/app/login/login.service';
import { User } from '../user.model';
import { UserService } from '../user.service';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})
export class UserEditComponent implements OnInit {
  user = User.empty()
  errorMessage = ''
  loading = false
  userType = this.cookieService.get("user").toLowerCase()

  constructor(private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private cookieService: CookieService,
    private loginService: LoginService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => {
      this.user.name = params["name"]
      this.user.email = params["email"]
    })
  }

  async onSubmit() {
    this.errorMessage = ''
    this.loading = true
    let oldUser = User.empty()
    this.userService.getMyUser()
    this.userService.userSelected.subscribe((user: User) => {
      oldUser = user
    })

    await this.delay()
    if (this.errorMessage) {
      this.loginService.onLoginExpried(this.errorMessage)
      this.loading = false
      return
    }

    else {
      if (this.user.password === oldUser.password) {
        const updateUser = new User(oldUser.id, this.user.name, this.user.email, this.user.confirmPassword, oldUser.type)

        this.userService.updateUser(this.cookieService.get("token"), 0, updateUser)
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
        this.errorMessage = 'wrong password!'
        this.loading = false
        return
      }
    }
  }

  onCancel() {
    this.router.navigate(["/user"])
  }

  private delay() {
    return new Promise<void>(resolve => {
      setTimeout(() => {
        resolve();
      }, 2000);
    })
  }
}