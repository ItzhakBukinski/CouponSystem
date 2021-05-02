import { EventEmitter, Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { StorageService } from "../common/storage.service";
import { Coupon } from "./coupon.model";
@Injectable()
export class CouponService {
    coupon: Coupon
    coupons: Coupon[]
    couponSelected = new EventEmitter<Coupon>()
    couponsChanged = new EventEmitter<Coupon[]>()
    errorChannel = new Subject<string>()

    constructor(private storageService: StorageService) { }

    fetchAllCoupons(token: string, sortType: number) {
        this.storageService.getAllCoupons(token, sortType).subscribe(coupons => {
            this.coupons = coupons
            this.onCouponsChanged()
        },
            error => {
                this.checkError(error)
            })
    }

    fetchAllCouponsByCategory(token: string, category: string) {
        this.storageService.getAllCouponsByCategory(token, category).subscribe(coupons => {
            this.coupons = coupons
            this.onCouponsChanged()
        },
            error => {
                this.checkError(error)
            })
    }

    fetchAllCouponsBeforeDate(token: string, date: string) {
        this.storageService.getAllCouponsBeforeDate(token, date).subscribe(coupons => {
            this.coupons = coupons
            this.onCouponsChanged()
        },
            error => {
                this.checkError(error)
            })
    }

    fetchAllCouponsLessThanPrice(token: string, price: number) {
        this.storageService.getAllCouponsLessThanPrice(token, price).subscribe(coupons => {
            this.coupons = coupons
            this.onCouponsChanged()
        },
            error => {
                this.checkError(error)
            })
    }

    fetchNotPurchasedCoupons(token: string) {
        this.storageService.getAllNotPurchasedCoupons(token).subscribe(coupons => {
            this.coupons = coupons
            this.onCouponsChanged()
        },
            error => {
                this.checkError(error)
            })
    }

    purchaseCoupon(token: string, id: number) {
        this.storageService.purchaseCoupon(token, id).subscribe(coupon => {
            this.coupons.push(coupon)
            this.onCouponsChanged()
        }, error => {
            this.checkError(error)
        })
    }

    appendCoupon(token: string, coupon: Coupon) {
        this.storageService.addCoupon(token, coupon).subscribe(coupon => {
            this.coupons.push(coupon)
            this.onCouponsChanged()
        }, error => {
            this.checkError(error)
        })
    }

    updateCoupon(token: string, coupon: Coupon, id: number) {
        this.storageService.updateCoupon(token, coupon).subscribe(coupon => {
            this.coupons[id] = coupon
            this.onCouponsChanged()
        },
            error => {
                this.checkError(error)
            })
    }

    deleteCoupon(token: string, id: number, coupon: Coupon) {
        this.storageService.deleteCoupon(token, coupon.id).subscribe(() => {
            this.coupons.splice(id, 1)
            this.onCouponsChanged()
        }, error => {
            this.checkError(error)
        })
    }

    deleteAllCoupons(token: string): void {
        this.storageService.deleteAllCoupons(token).subscribe(() => {
            this.coupons = []
            this.onCouponsChanged()
        }, error => {
            this.checkError(error)
        })
    }


    getCouponById(id: number) {
        return new Coupon(
            this.coupons[id].id,
            this.coupons[id].title,
            this.coupons[id].description,
            this.coupons[id].category,
            this.coupons[id].amount,
            this.coupons[id].startDate,
            this.coupons[id].endDate,
            this.coupons[id].price,
            this.coupons[id].imageURL,
            this.coupons[id].companyId,
            this.coupons[id].logo,
            this.coupons[id].sales
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

    private onCouponsChanged() {
        this.couponsChanged.emit(this.coupons)
        this.couponSelected.emit(this.coupon)
    }
}