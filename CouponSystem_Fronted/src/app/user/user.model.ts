import { Type } from "./type.model"

export class User {
    user = ''
    id = 0
    name = ""
    email = ""
    password = ""
    type: Type
    editedPassword = ''
    confirmPassword = ''


    constructor(id: number, name: string, email: string, password: string, type: Type) {
        this.user = type.toString()
        this.id = id
        this.name = name
        this.email = email
        this.password = password
        this.type = type
    }
    static empty() {
        return new User(0, '', '', '', Type.DEFAULT)
    }
}
