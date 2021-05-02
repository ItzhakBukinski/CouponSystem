package com.jb.couponSystem.service;

import com.jb.couponSystem.data.entity.Category;
import com.jb.couponSystem.data.entity.Coupon;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * A service for CRUD actions on coupons. In each action it is important to know which user is trying to do this action.
 * Some methods are restricted to some users (for example-purchasing is allowed just for customers), and others are
 * open for the miscellaneous users but the return data will change.
 * Note that in the Get methods there is an option the get them openly -meaning - not as some user (without any token),
 * in this case the "id" for an non-user by default will be 0.
 * The reason for this option is to be able to see all coupons openly in the homepage without login.
 */

public interface CouponService {

    /**
     * Finds all the coupons. Admin/no user- all in system, Company- all that are its own
     * and Customer- all that he purchased. Than sorting the list by a given sort way.
     *
     * @param sortType the way to sort.
     * @return a sorted list of the coupons.
     */
    List<Coupon> getAllCoupons(long userId, int sortType);

    /**
     * Finds all the coupons not already purchased by customer. Another users can't invoke this method.
     * The list will be sorted by the endDate of coupons by default.
     *
     * @return a sorted list by endDate of the coupons.
     */
    List<Coupon> getAllCustomerNotPurchasedCoupons(long customerId);

    /**
     * Finds all the coupons, from a given category. No user- all in system, customer- all that is available.
     * A company or an admin cannot invoke this method because it's irrelevant for them.
     * The list will be sorted by the endDate of coupons by default.
     *
     * @param userId   the id of the customer, or 0 in case of no-user.
     * @param category the given category.
     * @return a sorted list by endDate of the coupons.
     */
    List<Coupon> getAllCouponsByCategory(long userId, String category);

    /**
     * Finds all the coupons, that are cheaper than a given price. No user- all in system,
     * customer- all that is available. A company or an admin cannot invoke this method because it's irrelevant for them.
     * The list will be sorted by the endDate of coupons by default.
     *
     * @param userId the id of the customer, or 0 in case of no-user.
     * @param price  the given price.
     * @return a sorted list by endDate of the coupons.
     */
    List<Coupon> getAllCouponsLessThanPrice(long userId, double price);

    /**
     * Finds all the coupons, that their last date ends before a given date. No user- all in system,
     * customer- all that is available. A company or an admin cannot invoke this method because it's irrelevant for them.
     * The list will be sorted by the endDate of coupons by default.
     *
     * @param userId the id of the customer, or 0 in case of no-user.
     * @param date   the given date.
     * @return a sorted list by endDate of the coupons.
     */
    List<Coupon> getAllCouponsBeforeDate(long userId, LocalDate date);

    /**
     * Finds a single coupon by its id. Admin- any in system, company- one that is its own.
     * Customer can't invoke this method.
     *
     * @param userId   the id of the user.
     * @param couponId the id of the coupon.
     * @return an {@link Optional}. if found- contains a coupon, otherwise empty.
     */
    Optional<Coupon> getCouponById(long userId, long couponId);

    /**
     * Adding new coupon to the system. If the title is already exists in some coupon of the company,
     * or the amount is non-positive, or the endDate has already passed or the category is not one of the {@link Category}
     * values - it will not succeed. Only a company can invoke this method.
     *
     * @param companyId the id of the company.
     * @param coupon    the new coupon.
     * @return an {@link Optional}. if succeed- contains a coupon, otherwise empty.
     */
    Optional<Coupon> addCoupon(long companyId, Coupon coupon);

    /**
     * Purchasing a new coupon by the customer, another users can't invoke this method.
     * The customer cannot purchase a coupon that he already purchased or a one that is out of stock.
     *
     * @param customerId the id of the customer.
     * @param couponId   the id of the coupon.
     * @return an {@link Optional}. if succeed- contains a coupon, otherwise empty.
     */
    Optional<Coupon> purchaseCoupon(long customerId, long couponId);

    /**
     * Updating a coupon in the system. Only company can invoke this method.
     * Changing the original title of the coupon ,or the endDate to a date that already passed, is not allowed.
     * Note: the id cannot be changed ,and is used to find the original coupon in system.
     *
     * @param companyId the id of the company.
     * @param coupon    the coupon with the new details.
     * @return an {@link Optional}. if succeed- contains a coupon, otherwise empty.
     */
    Optional<Coupon> updateCoupon(long companyId, Coupon coupon);

    /**
     * Deleting a coupon. If the coupon does not exists nothing will be deleted.
     * Admin- any in system, company- one that is its own. Customer can't invoke this method.
     *
     * @param userId   the id of the user.
     * @param couponId the id of the coupon.
     */
    void deleteCoupon(long userId, long couponId);

    /**
     * Deleting all coupons. Admin- all in system, company- all that are its own.
     * Customer can't invoke this method.
     *
     * @param userId the id of the user.
     */
    void deleteAllCoupons(long userId);

    /**
     * A default method that invokes once a day at midnight, and deleting all expired coupons.
     */
    @Scheduled(cron = "1 0 0 * * *")
    default void deleteExpiredCoupons() {
        getAllCoupons(0, 0).stream()
                .filter(coupon -> coupon.getEndDate().isBefore(LocalDate.now()))
                .mapToLong(Coupon::getId)
                .forEach(couponId -> deleteCoupon(0, couponId));

    }
}
