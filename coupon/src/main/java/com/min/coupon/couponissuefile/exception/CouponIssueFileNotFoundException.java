package com.min.coupon.couponissuefile.exception;

public class CouponIssueFileNotFoundException extends CouponFileException {

    private static final String CODE = "FILE_NOT_FOUND";

    public CouponIssueFileNotFoundException(Long id) {
        super(CODE, "파일을 찾을 수 없습니다. id=" + id);
    }
}
