package com.min.coupon.couponissuefile.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class CouponIssueFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String storedFileName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponFileType fileType;

    @Column(nullable = false)
    private int customerCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected CouponIssueFile() {
    }

    public CouponIssueFile(String originalFileName, String storedFileName, CouponFileType fileType, int customerCount) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.fileType = fileType;
        this.customerCount = customerCount;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public CouponFileType getFileType() {
        return fileType;
    }

    public int getCustomerCount() {
        return customerCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
