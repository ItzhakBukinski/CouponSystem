import { EventEmitter, Injectable } from "@angular/core";
import { CookieService } from "ngx-cookie-service";
import { Subject } from "rxjs";
import { StorageService } from "../common/storage.service";
import { Type } from "./type.model";
import { User } from "./user.model";

@Injectable()
export class UserService {
    user: User
    users: User[]
    userSelected = new EventEmitter<User>()
    usersSelected = new EventEmitter<User[]>()
    errorChannel = new Subject<string>()
    constructor(private storageService: StorageService, private cookieService: CookieService) { }

    getMyUser() {
        this.storageService.getMyUser(this.cookieService.get("token")).subscribe(user => {
            this.user = user
            this.onUserChanged()
        },
            error => {
                this.checkError(error)
            })
    }

    fetchAllUsers(token: string) {
        this.storageService.getAllUsers(token).subscribe((users: User[]) => {
            this.users = users
            this.onUserChanged()
        }, error => {
            this.checkError(error)
        })
    }

    createUser(type: Type, name: string, email: string, password: string) {
        this.storageService.createUser(type, name, email, password).subscribe((user: User) => {
            this.user = user
            this.onUserChanged()
        }, error => {
            this.checkError(error)
        })
    }

    updateUser(token: string, id: number, user: User) {
        this.storageService.updateUser(token, user).subscribe(user => {
            this.user = user
            this.users[id] = user
            this.onUserChanged()
        }, error => {
            this.checkError(error)
        })
    }

    deleteUser(token: string, id: number, userId: number) {
        this.storageService.deleteUser(token, userId).subscribe(() => {
            this.users.splice(id, 1)
            this.onUserChanged()
        }, error => {
            this.checkError(error)
        })
    }

    getUserById(id: number) {
        return new User(
            this.users[id].id,
            this.users[id].name,
            this.users[id].email,
            this.users[id].password,
            this.users[id].type,

        )
    }

    private checkError(error: any) {
        if (error.error instanceof (ProgressEvent)) {
            this.errorChannel.next("There is a problem with server try later")
        }
        else {
            this.errorChannel.next(error.error.message)
        }
    }

    private onUserChanged() {
        this.userSelected.emit(this.user)
        this.usersSelected.emit(this.users)
    }
}