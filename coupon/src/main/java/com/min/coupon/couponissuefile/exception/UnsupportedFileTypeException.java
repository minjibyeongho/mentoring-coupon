package com.min.coupon.couponissuefile.exception;

public class UnsupportedFileTypeException extends CouponFileException {

    private static final String CODE = "UNSUPPORTED_FILE_TYPE";

    public UnsupportedFileTypeException(String fileName) {
        super(CODE, "지원하지 않는 파일 형식입니다: " + fileName);
    }
}
