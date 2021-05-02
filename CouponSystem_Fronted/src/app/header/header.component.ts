import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { LoginService } from '../login/login.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  logged = false
  loading = false
  errorMessage = ''
  constructor(private loginService: LoginService, private cookeiService: CookieService, private router: Router) { }

  ngOnInit(): void {
    this.logged = this.cookeiService.check("token")
    this.loginService.loggedStatus.subscribe((status: boolean) => {
      this.logged = status
    })
  }

  async onLogout() {
    this.errorMessage = ''
    this.loading = true

    this.loginService.errorChannel.subscribe((errorMessage: string) => {
      if (this.logged) {
        this.errorMessage = errorMessage
      }
    })

    this.loginService.logout()
    await this.delay()
    this.loading = false
    if (!this.errorMessage) {
      this.router.navigate(["/login"])
      this.logged = false
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

