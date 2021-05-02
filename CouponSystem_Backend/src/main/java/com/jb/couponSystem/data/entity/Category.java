package com.jb.couponSystem.data.entity;

import java.util.Arrays;

/**
 * An enumeration for the different categories for a {@link Coupon}.
 */
public enum Category {
    CLOTHES,
    FURNITURE,
    COMPUTERS,
    TOYS,
    ELECTRONICS,
    FOOD;

    /**
     * Checks if a given name of category is not a valid category.
     *
     * @param categoryName the given category name
     * @return true if the categoryName is not valid, otherwise false.
     */
    public static boolean isNotValid(String categoryName) {
        return Arrays.stream(values())
                .map(Category::name)
                .noneMatch(categoryName::equalsIgnoreCase);

    }
}
