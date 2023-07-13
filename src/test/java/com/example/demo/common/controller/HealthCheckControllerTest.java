package com.example.demo.common.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@SpringBootTest
class HealthCheckControllerTest {

    @Autowired
    MockMvc mockMvc;

    @DisplayName("health_check.html에 접근하면 200을 반환한다.")
    @Test
    void healthCheckTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/health_check.html"))
                .andExpect(status().isOk());
    }

}