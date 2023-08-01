package com.example.demo.user.controller;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.controller.reponse.UserResponse;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserCreateControllerTest {

    @DisplayName("유저를 생성하고 UserResponse를 반환한다.")
    @Test
    void userCreateTest() {
        TestContainer testContainer = TestContainer.builder()
                                                   .clockHolder(new TestClockHolder(1678530673951L))
                                                   .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                                                   .build();
//        UserCreateController userCreateController = UserCreateController.builder()
//                                                                        .userCreateService(testContainer.userCreateService)
//                                                                        .build();


        UserCreate userCreate = UserCreate.builder()
                                          .nickname("test")
                                          .address("seoul")
                                          .email("test@test.com")
                                          .build();

        ResponseEntity<UserResponse> result = testContainer.userCreateController.createUser(userCreate);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getBody().getNickname()).isEqualTo("test");
        assertThat(result.getBody().getEmail()).isEqualTo("test@test.com");
        assertThat(result.getBody().getLastLoginAt()).isNull();



    }

}