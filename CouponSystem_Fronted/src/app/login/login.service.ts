import { EventEmitter, Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { CookieService } from "ngx-cookie-service";
import { Subject } from "rxjs";
import { StorageService } from "../common/storage.service";
import { User } from "../user/user.model";

@Injectable()
export class LoginService {
  userSelected = new EventEmitter<User>()
  tokenSelected = new EventEmitter<string>()
  loggedStatus = new EventEmitter<boolean>()
  errorChannel = new Subject<string>()
  loginExpiredMessage = "Your login time had expired! please log in again"

  constructor(
    private storageService: StorageService,
    private cookieService: CookieService,
    private router: Router
  ) { }

  login(email: string, password: string, type: string) {
    this.storageService.login(email, password, type).subscribe(token => {
      this.cookieService.set("token", token)
      this.cookieService.set("user", type.toString())
      this.loggedStatus.emit(true)
    },
      error => {
        if (error.error instanceof (ProgressEvent)) {
          this.errorChannel.next("There is a problem with server try later")
        }
        else {
          this.errorChannel.next(error.error.message)
        }
      })
  }

  logout() {
    this.storageService.logout(this.cookieService.get("token")).subscribe(() => {
      this.cookieService.delete("token")
      this.cookieService.delete("user")
      this.loggedStatus.emit(false)
    },
      _error => {
        this.errorChannel.next("There is a problem with server please try later")
      })
  }

  /**
   * invoked everytime there is a logged user makes a request to the backend and there is an error. 
   * it checks if the error caused becauase of long time passed after the last action and thus the backend
   * deleted the user's token. it can be only if the error message is: "Your login time had expired!",
   * because this is the message from backend in this case. so in this case this method will do
   * logout and navigate to login with this error.
   * @param errorMessage the error message
   */
  onLoginExpried(errorMessage: string) {
    if (errorMessage === this.loginExpiredMessage) {
      this.router.navigate(["/login", errorMessage])
      this.logout()
      this.loggedStatus.emit(false)
    }
  }
}