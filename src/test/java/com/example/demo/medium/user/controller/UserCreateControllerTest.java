package com.example.demo.medium.user.controller;

import com.example.demo.user.domain.UserCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SpringBootTest
class UserCreateControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private JavaMailSender mailSender;

    @DisplayName("유저를 생성하고 UserResponse를 반환한다.")
    @Test
    void userCreateTest() throws Exception {
        //given
        UserCreate userCreate = UserCreate.builder()
                                          .email("test5@test.com")
                                          .nickname("test5")
                                          .address("NewYork")
                                          .build();
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        //when
        //then
        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                                                        .content(objectMapper.writeValueAsString(userCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test5@test.com"))
                .andExpect(jsonPath("$.nickname").value("test5"))
//                .andExpect(jsonPath("$.address").value("NewYork"))
                .andExpect(jsonPath("$.status").value("PENDING")) // 처음 유저를 만들면 PENDING 상태
               ;
    }

}