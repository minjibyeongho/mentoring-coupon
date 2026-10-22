package com.min.coupon.couponissuefile.web;

import com.min.coupon.couponissuefile.exception.CouponIssueFileNotFoundException;
import com.min.coupon.couponissuefile.exception.InvalidCouponFileException;
import com.min.coupon.couponissuefile.exception.UnsupportedFileTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CouponIssueFileExceptionHandler {

    @ExceptionHandler(InvalidCouponFileException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCouponFile(InvalidCouponFileException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(UnsupportedFileTypeException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedFileType(UnsupportedFileTypeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(CouponIssueFileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCouponIssueFileNotFound(CouponIssueFileNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getCode(), ex.getMessage()));
    }
}
