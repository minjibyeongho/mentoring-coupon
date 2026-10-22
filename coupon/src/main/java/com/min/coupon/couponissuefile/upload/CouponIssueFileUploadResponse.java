package com.min.coupon.couponissuefile.upload;

import java.time.LocalDateTime;

public record CouponIssueFileUploadResponse(
        Long id,
        String originalFileName,
        int customerCount,
        LocalDateTime uploadedAt
) {
}
