import { Component, OnInit } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { LoginService } from 'src/app/login/login.service';
import { User } from '../user.model';
import { UserService } from '../user.service';

@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.css']
})
export class UsersListComponent implements OnInit {
  users: User[]
  errorMessage = ''
  loading = false
  admin: User

  constructor(
    private cookieService: CookieService,
    private userService: UserService,
    private loginService: LoginService) { }

  async ngOnInit(): Promise<void> {
    this.userService.getMyUser()
    this.userService.errorChannel.subscribe(errorMessage => {
      this.errorMessage = errorMessage
    })
    this.userService.userSelected.subscribe((user: User) => {
      this.admin = user
    })
    this.userService.usersSelected.subscribe((users: User[]) => {
      this.users = users
    })

    this.userService.fetchAllUsers(this.cookieService.get("token"))
    await this.delay()
    if (this.errorMessage) {
      this.loginService.onLoginExpried(this.errorMessage)
    }
  }
  async onDelete(id: number) {
    this.errorMessage = ''
    if (confirm("are you sure you want to delete this user?")) {
      if (prompt("please enter your password") === this.admin.password) {
        this.loading = true
        this.userService.deleteUser(this.cookieService.get("token"), id, this.userService.getUserById(id).id)
        await this.delay()
        this.loading = false
        if (this.errorMessage) {
          this.loginService.onLoginExpried(this.errorMessage)
        }
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
