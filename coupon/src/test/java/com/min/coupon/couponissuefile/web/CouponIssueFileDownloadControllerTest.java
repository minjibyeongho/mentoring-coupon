package com.min.coupon.couponissuefile.web;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CouponIssueFileDownloadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void download_returns_originally_uploaded_file() throws Exception {
        byte[] content = "customer_id\n11111\n22222\n".getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile("file", "customers.csv", "text/csv", content);

        MvcResult uploadResult = mockMvc.perform(multipart("/api/coupon-issue-files").file(file))
                .andExpect(status().isCreated())
                .andReturn();

        long id = objectMapper.readTree(uploadResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/coupon-issue-files/{id}/file", id))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, containsString("customers.csv")))
                .andExpect(content().bytes(content));
    }

    @Test
    void download_fails_when_file_does_not_exist() throws Exception {
        mockMvc.perform(get("/api/coupon-issue-files/{id}/file", 999_999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("FILE_NOT_FOUND"));
    }
}
