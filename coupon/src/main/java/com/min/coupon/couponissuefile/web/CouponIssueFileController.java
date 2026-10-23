package com.min.coupon.couponissuefile.web;

import com.min.coupon.couponissuefile.download.CouponFileDownload;
import com.min.coupon.couponissuefile.download.CouponFileDownloadService;
import com.min.coupon.couponissuefile.upload.CouponFileUploadService;
import com.min.coupon.couponissuefile.upload.CouponIssueFileUploadResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/coupon-issue-files")
public class CouponIssueFileController {

    private final CouponFileUploadService uploadService;
    private final CouponFileDownloadService downloadService;

    public CouponIssueFileController(CouponFileUploadService uploadService,
                                      CouponFileDownloadService downloadService) {
        this.uploadService = uploadService;
        this.downloadService = downloadService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CouponIssueFileUploadResponse> upload(@RequestParam("file") MultipartFile file) {
        CouponIssueFileUploadResponse response = uploadService.upload(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        CouponFileDownload download = downloadService.download(id);
        String encodedFileName = URLEncoder.encode(download.originalFileName(), StandardCharsets.UTF_8)
                .replace("+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + download.originalFileName() + "\"; filename*=UTF-8''" + encodedFileName)
                .body(download.resource());
    }
}
