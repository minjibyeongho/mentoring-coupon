package com.min.coupon.couponissuefile.file;

import com.min.coupon.couponissuefile.exception.UnsupportedFileTypeException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CouponFileParserResolverTest {

    private final CouponFileParserResolver resolver =
            new CouponFileParserResolver(List.of(new CsvCouponFileParser(), new ExcelCouponFileParser()));

    @Test
    void resolve_returns_csv_parser() {
        CouponFileParser parser = resolver.resolve("customers.csv");

        assertThat(parser).isInstanceOf(CsvCouponFileParser.class);
    }

    @Test
    void resolve_returns_excel_parser() {
        CouponFileParser parser = resolver.resolve("customers.xlsx");

        assertThat(parser).isInstanceOf(ExcelCouponFileParser.class);
    }

    @Test
    void resolve_throws_when_file_type_is_unsupported() {
        assertThatThrownBy(() -> resolver.resolve("customers.txt"))
                .isInstanceOf(UnsupportedFileTypeException.class);
    }
}
