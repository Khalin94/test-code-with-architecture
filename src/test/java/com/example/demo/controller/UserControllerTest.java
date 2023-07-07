package com.example.demo.controller;

import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserResponse;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SqlGroup({
        @Sql(value = "/sql/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

})
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("사용자 정보를 조회한다.")
    @Test
    void getUserByIdTest() throws Exception {
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.nickname").value("test"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
        ;
    }

    @DisplayName("사용자 정보가 없으면 Exception이 발생한다.")
    @Test
    void getUserByIdExceptionTest() throws Exception {
        mockMvc.perform(get("/api/users/100"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Users에서 ID 100를 찾을 수 없습니다."))
        ;
    }

    @DisplayName("인증코드로 계정을 활성화 시킬 수 있다.")
    @Test
    void verifyEmailTest() throws Exception {
        mockMvc.perform(get("/api/users/2/verify").queryParam("certificationCode", "bbbb-bbbbb-bbba"))
               .andExpect(status().isFound())
        ;

        Optional<UserEntity> userEntity = userRepository.findById(2L);

        assertThat(userEntity.get().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @DisplayName("email을 통해 사용자 정보를 조회한다.")
    @Test
    void meTest() throws Exception {
        mockMvc.perform(get("/api/users/me").header("EMAIL", "test@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.nickname").value("test"))
                .andExpect(jsonPath("$.address").value("seoul"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
               ;
    }

    @DisplayName("email을 통해 내정보를 찾지 못하면 Exception이 발생한다.")
    @Test
    void meExceptionTest() throws Exception {
//        mockMvc.perform(get("/api/users/me").header("EMAIL", "wrong@test.com"))
//                .andExpect(status().isNotFound())
//               ;
        mockMvc.perform(get("/api/users/me").header("EMAIL", "wrong@test.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Users에서 ID wrong@test.com를 찾을 수 없습니다."))
        ;
    }

    @DisplayName("사용자 정보를 수정한다.")
    @Test
    void updateUserTest() throws Exception {
        //given
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                                                   .address("kimpo")
                                                   .nickname("hello")
                                                   .build();

        //when
        //then
        mockMvc.perform(put("/api/users/me").header("EMAIL", "test@test.com")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(jsonPath("$.nickname").value("hello"))
                .andExpect(jsonPath("$.address").value("kimpo"))
               ;

    }

}