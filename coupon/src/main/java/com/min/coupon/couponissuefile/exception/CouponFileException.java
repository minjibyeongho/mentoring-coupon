package com.min.coupon.couponissuefile.exception;

public abstract class CouponFileException extends RuntimeException {

    private final String code;

    protected CouponFileException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
