package com.min.coupon.couponissuefile.file;

import com.min.coupon.couponissuefile.exception.UnsupportedFileTypeException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CouponFileParserResolver {

    private final List<CouponFileParser> parsers;

    public CouponFileParserResolver(List<CouponFileParser> parsers) {
        this.parsers = parsers;
    }

    public CouponFileParser resolve(String fileName) {
        return parsers.stream()
                .filter(parser -> parser.supports(fileName))
                .findFirst()
                .orElseThrow(() -> new UnsupportedFileTypeException(fileName));
    }
}
