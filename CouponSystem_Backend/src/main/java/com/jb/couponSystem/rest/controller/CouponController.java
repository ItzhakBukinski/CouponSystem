package com.jb.couponSystem.rest.controller;

import com.jb.couponSystem.data.entity.Coupon;
import com.jb.couponSystem.rest.ClientSession;
import com.jb.couponSystem.rest.controller.ex.*;
import com.jb.couponSystem.service.CouponService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/coupons")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__(@Autowired))
public class CouponController {
    private final CouponService service;
    private final Map<String, ClientSession> tokensMap;


    /**
     * Getting all coupons in system - depends on type of user. The coupons will be sorted by a given sort type.
     *
     * @param token    the token of the current session of the user. In case of no user- no token will be provided!
     * @param sortType the sortType of the coupons
     * @return a {@link ResponseEntity} with a list of the coupons.
     * @throws InvalidLoginException when the {@link ClientSession} of the user and its token are no longer valid.
     */
    @GetMapping("/get-coupons")
    public ResponseEntity<List<Coupon>> getAllCoupons(
            @RequestParam(required = false) String token,
            @RequestParam int sortType)
            throws InvalidLoginException {

        long userId = beginSession(token);
        List<Coupon> allCoupons = service.getAllCoupons(userId, sortType);

        return ResponseEntity.ok(allCoupons);
    }


    /**
     * Getting all not purchased coupons of the customer.
     *
     * @param token the token of the current session of the customer.
     * @return a {@link ResponseEntity} with a list of the coupons.
     * @throws InvalidLoginException when the {@link ClientSession} of the customer and its token are no longer valid.
     */
    @GetMapping("/get-unPurchased-coupons")
    public ResponseEntity<List<Coupon>> getCustomerNotPurchasedCoupons(@RequestParam String token)
            throws InvalidLoginException {

        long customerId = beginSession(token);
        List<Coupon> allMyCoupons = service.getAllCustomerNotPurchasedCoupons(customerId);

        return ResponseEntity.ok(allMyCoupons);
    }

    /**
     * Getting all coupons in system, that are from a given category  - depends on type of user.
     * In case of no user- no token will be provided!
     *
     * @param token    the token of the current session of the user.
     * @param category the given category
     * @return a {@link ResponseEntity} with a list of the coupons.
     * @throws InvalidLoginException when the {@link ClientSession} of the user and its token are no longer valid.
     */
    @GetMapping("/get-coupons-by-category")
    public ResponseEntity<List<Coupon>> getAllCouponsByCategory(@RequestParam(required = false) String token,
                                                                @RequestParam String category)
            throws InvalidLoginException {

        long userId = beginSession(token);
        List<Coupon> allCouponsByCategory = service.getAllCouponsByCategory(userId, category);

        return ResponseEntity.ok(allCouponsByCategory);
    }

    /**
     * Getting all coupons in system, that are cheaper than a given price.
     *
     * @param token the token of the current session of the user. In case of no user- no token will be provided!
     * @param price the given price.
     * @return a {@link ResponseEntity} with a list of the coupons.
     * @throws InvalidLoginException when the {@link ClientSession} of the user and its token are no longer valid.
     */
    @GetMapping("/get-coupons-less-than-price")
    public ResponseEntity<List<Coupon>> getAllCouponsLessThanPrice(@RequestParam(required = false) String token,
                                                                   @RequestParam double price)
            throws InvalidLoginException {

        long userId = beginSession(token);
        List<Coupon> allCouponsLessThanPrice = service.getAllCouponsLessThanPrice(userId, price);

        return ResponseEntity.ok(allCouponsLessThanPrice);
    }

    /**
     * Getting all coupons in system, that their end date is before a given date.
     *
     * @param token the token of the current session of the user. In case of no user- no token will be provided!
     * @param date  the given date.
     * @return a {@link ResponseEntity} with a list of the coupons.
     * @throws InvalidLoginException when the {@link ClientSession} of the user and its token are no longer valid.
     */
    @GetMapping("/get-coupons-before-date")
    public ResponseEntity<List<Coupon>> getAllCouponsBeforeDate(
            @RequestParam(required = false) String token,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate date)
            throws InvalidLoginException {

        long userId = beginSession(token);
        List<Coupon> allCouponsBeforeDate = service.getAllCouponsBeforeDate(userId, date);

        return ResponseEntity.ok(allCouponsBeforeDate);
    }

    /**
     * Adding a new coupon.
     *
     * @param token  the token of current session of company that is trying to add the new coupon.
     * @param coupon the coupon to add.
     * @return a {@link ResponseEntity} with the coupon.
     * @throws InvalidLoginException when the {@link ClientSession} of the company and its token are no longer valid.
     * @throws UnableToCRUDException if it is unable to add this coupon.
     */
    @PostMapping("/add-coupon")
    public ResponseEntity<Coupon> addCoupon(@RequestParam String token, @RequestBody Coupon coupon)
            throws InvalidLoginException, UnableToCRUDException {

        long companyId = beginSession(token);
        Optional<Coupon> optCoupon = service.addCoupon(companyId, coupon);

        if (optCoupon.isPresent())
            return ResponseEntity.ok(optCoupon.get());
        throw new UnableToCRUDException(
                "You already have coupon with this title or the end date you choose has already passed!"
        );
    }

    /**
     * Updating a coupon. Changing the title of the coupon is not allowed.
     *
     * @param token  the token of the current session of the company.
     * @param coupon the updated coupon.
     * @return a {@link ResponseEntity} with the updated coupon.
     * @throws InvalidLoginException when the {@link ClientSession} of the company and its token are no longer valid.
     * @throws UnableToCRUDException if fails to update the coupon.
     */
    @PatchMapping("/update-coupon")
    public ResponseEntity<Coupon> updateCoupon(@RequestParam String token, @RequestBody Coupon coupon)
            throws InvalidLoginException, UnableToCRUDException {
        long companyId = beginSession(token);
        Optional<Coupon> optCoupon = service.updateCoupon(companyId, coupon);

        if (optCoupon.isPresent())
            return ResponseEntity.ok(optCoupon.get());
        throw new UnableToCRUDException("Unable to update because you're trying to update the coupon's title " +
                "or the coupon's endDate is a date before now");
    }

    /**
     * Deleting a coupon.Must contain no more than 255 characters
     *
     * @param token    the token of the current session of the admin or company.
     * @param couponId the id of the coupon.
     * @throws InvalidLoginException when the {@link ClientSession} of the user and its token are no longer valid.
     */
    @DeleteMapping("/delete")
    public void deleteCoupon(@RequestParam String token, @RequestParam long couponId)
            throws InvalidLoginException {
        long userId = beginSession(token);
        service.deleteCoupon(userId, couponId);

    }

    /**
     * Deleting all coupons in system.
     *
     * @param token the token of the current session of the admin or company.
     * @throws InvalidLoginException when the {@link ClientSession} of the user and its token are no longer valid.
     */
    @DeleteMapping("/delete-all")
    public void deleteAllCoupons(@RequestParam String token)
            throws InvalidLoginException {
        long userId = beginSession(token);
        service.deleteAllCoupons(userId);

    }

    /**
     * Purchasing new coupon by the customer.
     *
     * @param token    the token of the current session of the customer.
     * @param couponId the id of coupon to purchase.
     * @return a {@link ResponseEntity} with the coupon which has purchased.
     * @throws InvalidLoginException when the {@link ClientSession} of the customer and its token are no longer valid.
     * @throws UnableToCRUDException when the purchase didn't succeed.
     */
    @PostMapping("/purchase-coupon")
    public ResponseEntity<Coupon> purchaseCoupon(@RequestParam String token, @RequestParam long couponId)
            throws InvalidLoginException, UnableToCRUDException {
        long customerId = beginSession(token);
        Optional<Coupon> optCoupon = service.purchaseCoupon(customerId, couponId);

        if (optCoupon.isPresent())
            return ResponseEntity.ok(optCoupon.get());
        throw new UnableToCRUDException(
                "You cannot purchase the coupon either because you've already purchased it or it is out of stock!"
        );
    }

    /**
     * Beginning the process of a session for an action being acted by a user, by getting his {@link ClientSession}
     * that linked to the his token and updating the last time access.
     * In case of no-user this process will not be acted, and instead of that will be returned 0 for no-user "id".
     *
     * @param token the token of the current session of the user. In case of no-user-will be null.
     * @return the id of the user that holds the given token, or 0 in case of no-user.
     * @throws InvalidLoginException when the {@link ClientSession} of the user and its token are no longer valid.
     */
    private long beginSession(@Nullable String token) throws InvalidLoginException {
        if (token == null)
            return 0;

        ClientSession clientSession = tokensMap.get(token);
        if (clientSession == null)
            throw new InvalidLoginException("Your login time had expired! please log in again");
        clientSession.access();
        return clientSession.getClientId();
    }
}
