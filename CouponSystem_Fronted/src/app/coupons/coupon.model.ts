export class Coupon {
    id: number
    title: string
    description: string
    category: string
    amount: number
    startDate: string
    endDate: string
    price: number
    imageURL: string
    companyId: number
    sales: number
    logo: string

    constructor(id: number, title: string, description: string,
        category: string, amount: number, startDate: string, endDate: string,
        price: number, imageurl: string, companyId: number, logo: string, sales: number) {
        this.id = id
        this.title = title
        this.description = description
        this.category = category
        this.amount = amount
        this.startDate = startDate
        this.endDate = endDate
        this.price = price
        this.imageURL = imageurl
        this.companyId = companyId
        this.logo = logo
        this.sales = sales
    }

    static empty() {
        return new Coupon(null, null, null, null, null, null, null, null, null, null, null, null)
    }
}