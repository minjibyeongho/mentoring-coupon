package com.min.coupon.couponissuefile.download;

import org.springframework.core.io.Resource;

public record CouponFileDownload(Resource resource, String originalFileName) {
}
