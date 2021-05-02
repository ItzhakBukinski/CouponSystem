import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { User } from 'src/app/user/user.model';
import { UserService } from 'src/app/user/user.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})

export class SignUpComponent implements OnInit {
  user = User.empty()
  success = false
  loading = false
  errorMessage = ''
  @ViewChild("f")
  contactForm: NgForm;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.userService.errorChannel.subscribe(errorMessage => {
      this.errorMessage = errorMessage
    })
  }

  async onSubmit() {
    this.errorMessage = ''
    this.loading = true
    this.userService.createUser(this.user.type, this.user.name, this.user.email, this.user.password)
    await this.delay()
    this.loading = false
    this.success = this.errorMessage ? false : true
    this.contactForm.reset()
  }

  onClear() {
    this.user = User.empty()
    this.contactForm.reset()
    this.errorMessage = ''
    this.success = false
  }

  private delay() {
    return new Promise<void>(resolve => {
      setTimeout(() => {
        resolve();
      }, 2000);
    })
  }
}
