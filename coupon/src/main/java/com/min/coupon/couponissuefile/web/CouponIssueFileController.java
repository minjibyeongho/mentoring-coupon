package com.min.coupon.couponissuefile.web;

import com.min.coupon.couponissuefile.upload.CouponFileUploadService;
import com.min.coupon.couponissuefile.upload.CouponIssueFileUploadResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/coupon-issue-files")
public class CouponIssueFileController {

    private final CouponFileUploadService uploadService;

    public CouponIssueFileController(CouponFileUploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CouponIssueFileUploadResponse> upload(@RequestParam("file") MultipartFile file) {
        CouponIssueFileUploadResponse response = uploadService.upload(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
