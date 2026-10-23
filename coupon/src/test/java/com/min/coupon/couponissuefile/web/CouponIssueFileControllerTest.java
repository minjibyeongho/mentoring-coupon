package com.min.coupon.couponissuefile.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CouponIssueFileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void upload_succeeds_with_valid_csv() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "customers.csv", "text/csv",
                "customer_id\n11111\n22222\n".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/coupon-issue-files").file(file))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.originalFileName").value("customers.csv"))
                .andExpect(jsonPath("$.customerCount").value(2));
    }

    @Test
    void upload_fails_when_header_is_invalid() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "customers.csv", "text/csv",
                "id\n11111\n".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/coupon-issue-files").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_HEADER"));
    }

    @Test
    void upload_fails_when_customer_list_is_empty() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "customers.csv", "text/csv",
                "customer_id\n".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/coupon-issue-files").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("EMPTY_CUSTOMER_LIST"));
    }

    @Test
    void upload_fails_when_file_type_is_unsupported() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "customers.txt", "text/plain",
                "customer_id\n11111\n".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/coupon-issue-files").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("UNSUPPORTED_FILE_TYPE"));
    }
}
