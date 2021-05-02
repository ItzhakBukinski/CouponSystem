package com.jb.couponSystem.service;

import com.jb.couponSystem.data.entity.*;
import com.jb.couponSystem.data.repository.CouponRepository;
import com.jb.couponSystem.data.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__(@Autowired))
public class CouponServiceProvider implements CouponService {
    private final CouponRepository couponRepo;
    private final UserRepository userRepo;
    private final static int SORT_BY_END_DATE = 0;
    private final static int SORT_BY_START_DATE = 1;
    private final static int SORT_BY_TITLE = 2;
    private final static int SORT_BY_SALES = 3;


    @Override
    public List<Coupon> getAllCoupons(long userId, int sortType) {
        Optional<User> optionalUser = userRepo.findById(userId);
        List<Coupon> coupons = new ArrayList<>();

        /*In case of no user or an admin get all coupons in system*/
        if (userId == 0 || optionalUser.isPresent() && optionalUser.get().getType() == ClientType.ADMIN) {
            coupons = couponRepo.findAll();

            /*In case of a company get all its coupons*/
        } else if (optionalUser.isPresent() && optionalUser.get().getType() == ClientType.COMPANY) {
            coupons = couponRepo.findByCompanyId(userId);

            /*In case of a customer get all its purchased coupons*/
        } else if (optionalUser.isPresent() && optionalUser.get().getType() == ClientType.CUSTOMER) {
            coupons = couponRepo.findByCustomersId(userId);
        }

        return sorted(coupons, sortType);
    }

    @Override
    public List<Coupon> getAllCustomerNotPurchasedCoupons(long customerId) {
        return sorted(removeCouponsOutOfStock(couponRepo.findNotYetPurchasedCoupons(customerId)), SORT_BY_END_DATE);
    }

    @Override
    public List<Coupon> getAllCouponsByCategory(long userId, String category) {
        List<Coupon> coupons;

        /*In case of no user get all coupons in system from this category**/
        if (userId == 0) {
            coupons = couponRepo.findByCategory(Category.valueOf(category.toUpperCase()));

            /*In case of a customer get all its not purchased coupons from this category*/
        } else {
            coupons = removeCouponsOutOfStock(couponRepo.
                    findNotYetPurchasedCouponsByCategory(Category.valueOf(category.toUpperCase()), userId));
        }

        return sorted(coupons, SORT_BY_END_DATE);
    }

    @Override
    public List<Coupon> getAllCouponsLessThanPrice(long userId, double price) {
        List<Coupon> coupons;

        /*In case of no user get all coupons in system less than the price*/
        if (userId == 0) {
            coupons = couponRepo.findByPriceLessThan(price);

            /*In case of a customer get all its not purchased coupons less than the price*/
        } else {
            coupons = removeCouponsOutOfStock(couponRepo
                    .findNotYetPurchasedCouponsLessThanPrice(price, userId));
        }

        return sorted(coupons, SORT_BY_END_DATE);
    }

    @Override
    public List<Coupon> getAllCouponsBeforeDate(long userId, LocalDate date) {
        List<Coupon> coupons;

        /*In case of no user get all coupons in system before the date*/
        if (userId == 0) {
            coupons = couponRepo.findByEndDateBefore(date);

            /*In case of a customer get all its not purchased coupons before the date*/
        } else {
            coupons = removeCouponsOutOfStock(couponRepo
                    .findNotYetPurchasedCouponsBeforeDate(date, userId));
        }

        return sorted(coupons, SORT_BY_END_DATE);
    }

    @Override
    public Optional<Coupon> getCouponById(long userId, long couponId) {
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent() && optionalUser.get().getType() == ClientType.COMPANY)
            return couponRepo.findById(couponId)
                    .filter(optCoupon -> optCoupon.getCompany().getId() == userId);

        return couponRepo.findById(couponId);
    }

    @Override
    public Optional<Coupon> addCoupon(long companyId, Coupon coupon) {
        /*Checking that the coupon to add is valid*/
        if (couponIsNotValid(coupon.getTitle(), coupon.getEndDate(), coupon.getCategory()
                .toString(), coupon.getAmount()))
            return Optional.empty();

        /*If valid- add the company to the coupon and save it*/
        Company company = (Company) getUser(companyId);
        coupon.setCompany(company);

        return Optional.of(couponRepo.save(coupon));
    }

    @Override
    public Optional<Coupon> purchaseCoupon(long customerId, long couponId) {
        Customer customer = (Customer) getUser(customerId);

        return couponRepo.findById(couponId)
                /*Checking that the customer didn't purchase it and that the coupon is not out of stock*/
                .filter(coupon -> customerNotYetPurchasedCoupon(customerId, couponId) && coupon.isInStock())
                .stream()
                .peek(streamedCoupon -> {
                    if (streamedCoupon != null) {
                        /*Updating the sales and amount of the coupon and adding the customer to the updated coupon*/
                        streamedCoupon.setAmount(streamedCoupon.getAmount() - 1);
                        streamedCoupon.setSales(streamedCoupon.getSales() + 1);
                        streamedCoupon.getCustomers().add(customer);
                        couponRepo.save(streamedCoupon);
                    }
                })
                .findAny();
    }

    @Override
    public Optional<Coupon> updateCoupon(long companyId, Coupon coupon) {
        Company company = (Company) getUser(companyId);

        return getCouponById(companyId, coupon.getId())
                .filter(optCoupon -> coupon.getTitle().equals(optCoupon.getTitle())
                        && LocalDate.now().isBefore(coupon.getEndDate())
                )
                .stream()
                .peek(streamedCoupon -> {
                    if (streamedCoupon != null) {
                        /*Saving the company and customers from the original coupon to the updated one*/
                        coupon.setCompany(company);
                        coupon.setCustomers(streamedCoupon.getCustomers());
                        couponRepo.save(coupon);
                    }
                })
                .findAny();
    }

    @Override
    public void deleteCoupon(long userId, long couponId) {
        Optional<Coupon> optCoupon = getCouponById(userId, couponId);
        /*Checking that coupon exists*/
        optCoupon.ifPresent(couponRepo::delete);
    }

    @Override
    public void deleteAllCoupons(long userId) {
        List<Coupon> allCoupons = getAllCoupons(userId, 0);
        couponRepo.deleteAll(allCoupons);
    }

    /**
     * Getting the user which holds a given id. Asserting that the user exists, because this method is invoked
     * only with a real id that exists to a user, and returning the user.
     *
     * @param userId the id of the user.
     * @return the user.
     */
    private User getUser(long userId) {
        Optional<User> optionalUser = userRepo.findById(userId);
        assert optionalUser.isPresent();
        return optionalUser.get();
    }

    /**
     * Sorting a list of coupons depended on given sort way.
     *
     * @param coupons  the list to sort.
     * @param sortType the way to sort.
     * @return the list sorted.
     */
    private List<Coupon> sorted(List<Coupon> coupons, int sortType) {
        return coupons.stream().sorted((o1, o2) -> {
            switch (sortType) {
                case SORT_BY_END_DATE:
                    return o1.getEndDate().compareTo(o2.getEndDate());

                case SORT_BY_START_DATE:
                    return o1.getStartDate().compareTo(o2.getStartDate());

                case SORT_BY_TITLE:
                    return o1.getTitle().compareToIgnoreCase(o2.getTitle());

                case SORT_BY_SALES:
                    return o2.getSales() - o1.getSales();

                default:
                    return 0;
            }
        }).collect(Collectors.toList());
    }

    /**
     * Removing coupons that are out of stock (amount=0) from a given list of coupons.
     *
     * @param coupons the given list of coupons.
     * @return the list after the removing.
     */
    private List<Coupon> removeCouponsOutOfStock(List<Coupon> coupons) {
        return coupons
                .stream()
                .filter(Coupon::isInStock)
                .collect(Collectors.toList());
    }

    /**
     * Checking if coupon's details are not valid. the details that are being checked are:
     * 1. The title- Should be a new title and not one that already exists at one of the company's coupons.
     * 2. The endDate- Should be not before the current day of the coupon creating.
     * 3. The categoryName is a{@link String} that should be checked that is exists in the {@link Category} values.
     * 4. The amount- Should be at least 10.
     *
     * @param titleName    the title of the new coupon
     * @param endDate      the endDate of the new coupon
     * @param categoryName the name of the category of the new coupon
     * @param amount       the amount of the new coupon
     * @return true if one of those conditions failed. Otherwise false.
     */
    private boolean couponIsNotValid(String titleName, LocalDate endDate, String categoryName, int amount) {

        /*Checking if the title already exists*/
        boolean titleExists = getAllCoupons(0, 0).stream()
                .map(Coupon::getTitle)
                .anyMatch(titleName::equalsIgnoreCase);

        return titleExists || Category.isNotValid(categoryName) || endDate.isBefore(LocalDate.now()) || amount < 10;
    }

    /**
     * Checking whether the customer already purchased a given coupon or not.
     *
     * @param couponId the id of the coupon.
     * @return true if not yet purchased, otherwise false.
     */
    private boolean customerNotYetPurchasedCoupon(long customerId, long couponId) {
        return getAllCustomerNotPurchasedCoupons(customerId)
                .stream()
                .mapToLong(Coupon::getId)
                .anyMatch(id -> id == couponId);
    }
}
