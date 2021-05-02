package com.jb.couponSystem.data.repository;

import com.jb.couponSystem.data.entity.Category;
import com.jb.couponSystem.data.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * A {@link JpaRepository} interface for the {@link Coupon} entity.
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    /**
     * Finds all coupons from a given category, in the system.
     *
     * @param category the given category
     * @return a list of the coupons.
     */
    List<Coupon> findByCategory(Category category);

    /**
     * Finds all coupons that are cheaper than a given price, in the system.
     *
     * @param price the given price.
     * @return a list of the coupons.
     */
    List<Coupon> findByPriceLessThan(double price);

    /**
     * Finds all coupons that their endDate is before a given date, in the system.
     *
     * @param endDate the given date.
     * @return a list of the coupons.
     */
    List<Coupon> findByEndDateBefore(LocalDate endDate);

    /**
     * Finds all coupons that belongs to a company.
     *
     * @param companyId the id of the company.
     * @return a list of the coupons.
     */
    List<Coupon> findByCompanyId(long companyId);

    /**
     * Finds all coupons that belongs to a customer.
     *
     * @param customerId the id of the customer.
     * @return a list of the coupons.
     */
    List<Coupon> findByCustomersId(long customerId);

    /**
     * Finds all coupons that in system and the customer did not purchased them yet.
     *
     * @param customerId the id of the customer.
     * @return a list of the coupons.
     */
    @Query("select b from Coupon b where b not in" +
            " (select cp from Coupon cp inner join cp.customers cs where cs.id= :customerId)")
    List<Coupon> findNotYetPurchasedCoupons(long customerId);

    /**
     * Finds all coupons that in system, from a given category, and the customer did not purchased them yet.
     *
     * @param category   the given category.
     * @param customerId the id of the customer.
     * @return a list of the coupons.
     */
    @Query("select b from Coupon b where b not in " +
            "(select cp from Coupon cp inner join cp.customers cs where cs.id= :customerId) and b.category= :category")
    List<Coupon> findNotYetPurchasedCouponsByCategory(Category category, long customerId);

    /**
     * Finds all coupons that in system, which their endDate is before a given date,
     * and the customer did not purchased them yet.
     *
     * @param endDate    the given date
     * @param customerId the id of the customer.
     * @return a list of the coupons.
     */
    @Query("select b from Coupon b where b not in " +
            "(select cp from Coupon cp inner join cp.customers cs where cs.id= :customerId) and b.endDate < :endDate")
    List<Coupon> findNotYetPurchasedCouponsBeforeDate(LocalDate endDate, long customerId);

    /**
     * Finds all coupons that in system, that are cheaper than a given price,
     * and the customer did not purchased them yet.
     *
     * @param price      the given price.
     * @param customerId the id of the customer.
     * @return a list of the coupons.
     */
    @Query("select b from Coupon b where b not in " +
            "(select cp from Coupon cp inner join cp.customers cs where cs.id= :customerId) and b.price < :price")
    List<Coupon> findNotYetPurchasedCouponsLessThanPrice(double price, long customerId);
}
