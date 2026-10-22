package com.min.coupon.couponissuefile.file;

import java.io.InputStream;

public interface CouponFileParser {

    boolean supports(String fileName);

    ParsedCouponFile parse(InputStream inputStream);
}
