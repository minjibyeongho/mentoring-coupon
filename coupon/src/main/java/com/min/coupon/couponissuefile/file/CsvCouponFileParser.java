package com.min.coupon.couponissuefile.file;

import com.min.coupon.couponissuefile.exception.InvalidCouponFileException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Component
public class CsvCouponFileParser implements CouponFileParser {

    @Override
    public boolean supports(String fileName) {
        return fileName != null && fileName.toLowerCase(Locale.ROOT).endsWith(CouponFileConstants.CSV_EXTENSION);
    }

    @Override
    public ParsedCouponFile parse(InputStream inputStream) {
        CSVFormat csvFormat = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();

        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             CSVParser csvParser = csvFormat.parse(reader)) {

            List<String> headerNames = csvParser.getHeaderNames();
            if (headerNames.size() != 1 || !CouponFileConstants.CUSTOMER_ID_HEADER.equals(headerNames.get(0))) {
                throw InvalidCouponFileException.invalidHeader(CouponFileConstants.CUSTOMER_ID_HEADER);
            }

            List<CSVRecord> records = csvParser.getRecords();
            if (records.isEmpty()) {
                throw InvalidCouponFileException.emptyCustomerList();
            }

            return new ParsedCouponFile(records.size());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
