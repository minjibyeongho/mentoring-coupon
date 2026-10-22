package com.min.coupon.couponissuefile.file;

import com.min.coupon.couponissuefile.exception.InvalidCouponFileException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CsvCouponFileParserTest {

    private final CsvCouponFileParser parser = new CsvCouponFileParser();

    @Test
    void supports_csv_extension() {
        assertThat(parser.supports("customers.csv")).isTrue();
        assertThat(parser.supports("customers.xlsx")).isFalse();
    }

    @Test
    void parse_valid_csv() {
        ParsedCouponFile result = parser.parse(toInputStream("customer_id\n11111\n22222\n33333\n"));

        assertThat(result.customerCount()).isEqualTo(3);
    }

    @Test
    void parse_fails_when_header_is_invalid() {
        InputStream csv = toInputStream("id\n11111\n");

        assertThatThrownBy(() -> parser.parse(csv))
                .isInstanceOf(InvalidCouponFileException.class);
    }

    @Test
    void parse_fails_when_customer_list_is_empty() {
        InputStream csv = toInputStream("customer_id\n");

        assertThatThrownBy(() -> parser.parse(csv))
                .isInstanceOf(InvalidCouponFileException.class);
    }

    private InputStream toInputStream(String content) {
        return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    }
}
