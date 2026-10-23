package com.min.coupon.couponissuefile.download;

import com.min.coupon.couponissuefile.domain.CouponIssueFile;
import com.min.coupon.couponissuefile.exception.CouponIssueFileNotFoundException;
import com.min.coupon.couponissuefile.repository.CouponIssueFileRepository;
import com.min.coupon.couponissuefile.storage.FileStorageService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class CouponFileDownloadService {

    private final CouponIssueFileRepository repository;
    private final FileStorageService fileStorageService;

    public CouponFileDownloadService(CouponIssueFileRepository repository, FileStorageService fileStorageService) {
        this.repository = repository;
        this.fileStorageService = fileStorageService;
    }

    public CouponFileDownload download(Long id) {
        CouponIssueFile issueFile = repository.findById(id)
                .orElseThrow(() -> new CouponIssueFileNotFoundException(id));

        Resource resource = new FileSystemResource(fileStorageService.load(issueFile.getStoredFileName()));
        return new CouponFileDownload(resource, issueFile.getOriginalFileName());
    }
}
