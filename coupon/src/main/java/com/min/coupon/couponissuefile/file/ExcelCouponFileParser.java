package com.min.coupon.couponissuefile.file;

import com.min.coupon.couponissuefile.exception.InvalidCouponFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Locale;

@Component
public class ExcelCouponFileParser implements CouponFileParser {

    @Override
    public boolean supports(String fileName) {
        return fileName != null && fileName.toLowerCase(Locale.ROOT).endsWith(CouponFileConstants.EXCEL_EXTENSION);
    }

    @Override
    public ParsedCouponFile parse(InputStream inputStream) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            Row headerRow = sheet.getRow(0);
            Cell headerCell = headerRow == null ? null : headerRow.getCell(0);
            String headerValue = headerCell == null ? null : headerCell.getStringCellValue();
            if (!CouponFileConstants.CUSTOMER_ID_HEADER.equals(headerValue)) {
                throw InvalidCouponFileException.invalidHeader(CouponFileConstants.CUSTOMER_ID_HEADER);
            }

            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum < 1) {
                throw InvalidCouponFileException.emptyCustomerList();
            }

            return new ParsedCouponFile(lastRowNum);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
