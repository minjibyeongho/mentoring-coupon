package com.min.coupon.couponissuefile.upload;

import com.min.coupon.couponissuefile.domain.CouponFileType;
import com.min.coupon.couponissuefile.domain.CouponIssueFile;
import com.min.coupon.couponissuefile.file.CouponFileConstants;
import com.min.coupon.couponissuefile.file.CouponFileParser;
import com.min.coupon.couponissuefile.file.CouponFileParserResolver;
import com.min.coupon.couponissuefile.file.ParsedCouponFile;
import com.min.coupon.couponissuefile.repository.CouponIssueFileRepository;
import com.min.coupon.couponissuefile.storage.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Locale;

@Service
public class CouponFileUploadService {

    private final CouponFileParserResolver parserResolver;
    private final FileStorageService fileStorageService;
    private final CouponIssueFileRepository repository;

    public CouponFileUploadService(CouponFileParserResolver parserResolver,
                                    FileStorageService fileStorageService,
                                    CouponIssueFileRepository repository) {
        this.parserResolver = parserResolver;
        this.fileStorageService = fileStorageService;
        this.repository = repository;
    }

    public CouponIssueFileUploadResponse upload(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        CouponFileParser parser = parserResolver.resolve(originalFileName);

        ParsedCouponFile parsed = parse(parser, file);
        String storedFileName = store(originalFileName, file);

        CouponIssueFile savedFile = repository.save(new CouponIssueFile(
                originalFileName,
                storedFileName,
                resolveFileType(originalFileName),
                parsed.customerCount()
        ));

        return new CouponIssueFileUploadResponse(
                savedFile.getId(),
                savedFile.getOriginalFileName(),
                savedFile.getCustomerCount(),
                savedFile.getCreatedAt()
        );
    }

    private ParsedCouponFile parse(CouponFileParser parser, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            return parser.parse(inputStream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String store(String originalFileName, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            return fileStorageService.save(originalFileName, inputStream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private CouponFileType resolveFileType(String fileName) {
        if (fileName.toLowerCase(Locale.ROOT).endsWith(CouponFileConstants.CSV_EXTENSION)) {
            return CouponFileType.CSV;
        }
        return CouponFileType.EXCEL;
    }
}
