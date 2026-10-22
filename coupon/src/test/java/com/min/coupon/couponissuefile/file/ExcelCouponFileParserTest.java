package com.min.coupon.couponissuefile.file;

import com.min.coupon.couponissuefile.exception.InvalidCouponFileException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExcelCouponFileParserTest {

    private final ExcelCouponFileParser parser = new ExcelCouponFileParser();

    @Test
    void supports_excel_extension() {
        assertThat(parser.supports("customers.xlsx")).isTrue();
        assertThat(parser.supports("customers.csv")).isFalse();
    }

    @Test
    void parse_valid_excel() {
        InputStream excel = buildExcel("customer_id", "11111", "22222");

        ParsedCouponFile result = parser.parse(excel);

        assertThat(result.customerCount()).isEqualTo(2);
    }

    @Test
    void parse_fails_when_header_is_invalid() {
        InputStream excel = buildExcel("id", "11111");

        assertThatThrownBy(() -> parser.parse(excel))
                .isInstanceOf(InvalidCouponFileException.class);
    }

    @Test
    void parse_fails_when_customer_list_is_empty() {
        InputStream excel = buildExcel("customer_id");

        assertThatThrownBy(() -> parser.parse(excel))
                .isInstanceOf(InvalidCouponFileException.class);
    }

    private InputStream buildExcel(String header, String... customerIds) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue(header);

            for (int i = 0; i < customerIds.length; i++) {
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(customerIds[i]);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
