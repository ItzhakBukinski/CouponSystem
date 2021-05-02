import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Coupon } from "../coupons/coupon.model";
import { Type } from "../user/type.model";
import { User } from "../user/user.model";

@Injectable()
export class StorageService {
    constructor(private http: HttpClient) { }

    /* Login services */

    login(email: string, password: string, type: string) {
        const params = new HttpParams().set('email', email).set('password', password).set('type', type)
        return this.http.post<string>("http://localhost:8080/api/login", params)
    }

    logout(token: string) {
        const params = new HttpParams().set('token', token)
        return this.http.delete<string>("http://localhost:8080/api/logout", { params })
    }

    /* Users services */

    getMyUser(token: string) {
        const params = new HttpParams().set('token', token)
        return this.http.get<User>("http://localhost:8080/api/user/get-my-user", { params })
    }

    getAllUsers(token: string) {
        const params = new HttpParams().set('token', token)
        return this.http.get<User[]>("http://localhost:8080/api/user/get-users", { params })
    }

    createUser(type: Type, name: string, email: string, password: string) {
        const params = new HttpParams().set('type', type).set('name', name).set('email', email).set('password', password)
        return this.http.post<User>("http://localhost:8080/api/user/create", params)
    }

    updateUser(token: string, user: User) {
        const params = new HttpParams().set('token', token)
        return this.http.patch<User>("http://localhost:8080/api/user/update", user, { params })
    }

    deleteUser(token: string, userId: number) {
        const params = new HttpParams().set('token', token).set('userId', userId.toString())
        return this.http.delete<User>("http://localhost:8080/api/user/delete", { params })
    }

    /* Coupons services */

    /* In the next method there are 2 options: to get the coupons openly without a token for homepage,
   or with token -the user's owned coupons */
    getAllCoupons(token: string, sortType: number) {
        const urlPath = "http://localhost:8080/api/coupons/get-coupons"
        if (typeof token === 'undefined') {
            const params = new HttpParams().set('sortType', '0')
            return this.http.get<Coupon[]>(urlPath, { params })
        }
        const params = new HttpParams().set('token', token).set('sortType', sortType.toString())
        return this.http.get<Coupon[]>(urlPath, { params })
    }
    /* Here are ALL customer's available coupons */
    getAllNotPurchasedCoupons(token: string) {
        const params = new HttpParams().set('token', token)
        return this.http.get<Coupon[]>(`http://localhost:8080/api/coupons/get-unPurchased-coupons?token=${token}`)
    }

    /* In the next 3 methods there are 2 options: to get the coupons openly without a token for homepage,
    or with token - for the customer's available coupons */
    getAllCouponsByCategory(token: string, category: string) {
        const urlPath = "http://localhost:8080/api/coupons/get-coupons-by-category"
        if (typeof token === 'undefined') {
            const params = new HttpParams().set('category', category)
            return this.http.get<Coupon[]>(urlPath, { params })
        }
        const params = new HttpParams().set('token', token).set('category', category)
        return this.http.get<Coupon[]>(urlPath, { params })
    }

    getAllCouponsLessThanPrice(token: string, price: number) {
        const urlPath = "http://localhost:8080/api/coupons/get-coupons-less-than-price"
        if (typeof token === 'undefined') {
            const params = new HttpParams().set('price', price.toString())
            return this.http.get<Coupon[]>(urlPath, { params })
        }
        const params = new HttpParams().set('token', token).set('price', price.toString())
        return this.http.get<Coupon[]>(urlPath, { params })
    }

    getAllCouponsBeforeDate(token: string, date: string) {
        const urlPath = "http://localhost:8080/api/coupons/get-coupons-before-date"
        if (typeof token === 'undefined') {
            const params = new HttpParams().set('date', date)
            return this.http.get<Coupon[]>(urlPath, { params })
        }
        const params = new HttpParams().set('token', token).set('date', date)
        return this.http.get<Coupon[]>(urlPath, { params })
    }


    purchaseCoupon(token: string, id: number) {
        const params = new HttpParams().set('token', token).set('couponId', id.toString())
        return this.http.post<Coupon>("http://localhost:8080/api/coupons/purchase-coupon", params)
    }

    addCoupon(token: string, coupon: Coupon) {
        const params = new HttpParams().set('token', token)
        return this.http.post<Coupon>("http://localhost:8080/api/coupons/add-coupon", coupon, { params })
    }

    updateCoupon(token: string, coupon: Coupon) {
        const params = new HttpParams().set('token', token)
        return this.http.patch<Coupon>("http://localhost:8080/api/coupons/update-coupon", coupon, { params })
    }

    deleteCoupon(token: string, id: number) {
        const params = new HttpParams().set('token', token).set('couponId', id.toString())
        return this.http.delete<any>("http://localhost:8080/api/coupons/delete", { params })
    }

    deleteAllCoupons(token: string) {
        const params = new HttpParams().set('token', token)
        return this.http.delete<any>("http://localhost:8080/api/coupons/delete-all", { params })
    }
}