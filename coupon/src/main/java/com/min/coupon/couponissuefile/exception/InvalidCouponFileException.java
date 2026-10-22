package com.min.coupon.couponissuefile.exception;

public class InvalidCouponFileException extends CouponFileException {

    public static final String INVALID_HEADER = "INVALID_HEADER";
    public static final String EMPTY_CUSTOMER_LIST = "EMPTY_CUSTOMER_LIST";

    private InvalidCouponFileException(String code, String message) {
        super(code, message);
    }

    public static InvalidCouponFileException invalidHeader(String expectedHeader) {
        return new InvalidCouponFileException(INVALID_HEADER, "헤더값이 '" + expectedHeader + "'가 아닙니다.");
    }

    public static InvalidCouponFileException emptyCustomerList() {
        return new InvalidCouponFileException(EMPTY_CUSTOMER_LIST, "회원 목록이 비어있습니다.");
    }
}
