package com.jb.couponSystem.data.entity;

/**
 * An Enumeration for the types of clients in the system.
 */
public enum ClientType {
    /**
     * An admin in system. There is no way to create a new admin (just manually), but an existing one can update or
     * delete himself. An admin can delete any other user (company or customer) or any coupon but can't create or update them.
     */
    ADMIN,
    /**
     * A company can create and do actions just on the coupons that belongs to the company.
     * Also to create,update or delete itself.
     */
    COMPANY,
    /**
     * A customer can only purchase coupons and get information about his coupons and the other coupons in system.
     * Also to create,update or delete himself.
     */
    CUSTOMER

}
