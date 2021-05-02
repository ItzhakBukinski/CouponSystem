import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { User } from '../user/user.model';
import { LoginService } from './login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  user = User.empty()
  @ViewChild("f") contactForm: NgForm
  errorMessage: string
  loading = false

  constructor(private service: LoginService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => {
      this.errorMessage = params["error"]
    })
    this.service.errorChannel.subscribe(errormesage => {
      this.errorMessage = errormesage
    })
  }

  async onSubmit() {
    this.errorMessage = ''
    this.loading = true

    this.service.login(this.user.email, this.user.password, this.user.type)
    await this.delay()
    this.loading = false
    if (this.errorMessage) {
      this.user = User.empty()
      this.contactForm.reset()
      return
    }

    this.service.userSelected.emit(this.user)
    this.service.loggedStatus.emit(true)
    this.router.navigate(["/user"])
  }

  onClearLoginView() {
    this.user = User.empty()
    this.errorMessage = ''
    this.contactForm.reset()
  }

  private delay() {
    return new Promise<void>(resolve => {
      setTimeout(() => {
        resolve();
      }, 2000);
    })
  }
}
