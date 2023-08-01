package com.example.demo.user.controller;

import com.example.demo.common.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.controller.reponse.MyProfileResponse;
import com.example.demo.user.controller.reponse.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class UserControllerTest {

    @DisplayName("사용자 정보를 userId로 조회한다.")
    @Test
    void getUserByIdTest() {
        TestContainer testContainer = TestContainer.builder()
                                                   .clockHolder(new TestClockHolder(1678530673951L))
                                                   .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                                                   .build();
        testContainer.userRepository.save(User.builder()
                                              .address("seoul")
                                              .id(1L)
                                              .lastLoginAt(100L)
                                              .email("test@test.com")
                                              .status(UserStatus.ACTIVE)
                                              .nickname("test")
                                              .build());
        ResponseEntity<UserResponse> result = testContainer.userController.getUserById(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()
                         .getNickname()).isEqualTo("test");
        assertThat(result.getBody()
                         .getEmail()).isEqualTo("test@test.com");
        assertThat(result.getBody()
                         .getId()).isEqualTo(1L);
        assertThat(result.getBody()
                         .getStatus()).isEqualTo(UserStatus.ACTIVE);


    }

    @DisplayName("사용자 정보가 없으면 Exception이 발생한다.")
    @Test
    void getUserByIdExceptionTest() {
        TestContainer testContainer = TestContainer.builder()
                                                   .clockHolder(new TestClockHolder(1678530673951L))
                                                   .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                                                   .build();
        UserController userController = testContainer.userController;

        assertThatThrownBy(() -> {
            userController.getUserById(1);
        }).isInstanceOf(ResourceNotFoundException.class);

    }

    @DisplayName("인증코드로 계정을 활성화 시킬 수 있다.")
    @Test
    void verifyEmailTest() {
        TestContainer testContainer = TestContainer.builder()
                                                   .clockHolder(new TestClockHolder(1678530673951L))
                                                   .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                                                   .build();
        testContainer.userRepository.save(User.builder()
                                              .email("test@test.com")
                                              .nickname("test")
                                              .status(UserStatus.PENDING)
                                              .id(1L)
                                              .address("seoul")
                                              .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                                              .build());
        UserController userController = testContainer.userController;

        userController.verifyEmail(1, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        ResponseEntity<UserResponse> result = userController.getUserById(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()
                         .getStatus()).isEqualTo(UserStatus.ACTIVE);

    }

    @Test
    void verifyEmailExceptionTest() {
        TestContainer testContainer = TestContainer.builder()
                                                   .build();

        testContainer.userRepository.save(User.builder()
                                              .email("test@test.com")
                                              .nickname("test")
                                              .status(UserStatus.PENDING)
                                              .id(1L)
                                              .address("seoul")
                                              .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                                              .build());
        assertThatThrownBy(() -> {
            testContainer.userController.verifyEmail(1L, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);


    }

    @DisplayName("email을 통해 사용자 정보를 조회한다.")
    @Test
    void meTest() {
        TestContainer testContainer = TestContainer.builder()
                                                   .clockHolder(new TestClockHolder(1678530673951L))
                                                   .build();
        testContainer.userRepository.save(User.builder()
                                              .email("test@test.com")
                                              .nickname("test")
                                              .status(UserStatus.ACTIVE)
                                              .id(1L)
                                              .address("seoul")
                                              .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                                              .lastLoginAt(100L)
                                              .build());
        ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("test@test.com");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()
                         .getAddress()).isEqualTo("seoul");
        assertThat(result.getBody()
                         .getLastLoginAt()).isEqualTo(1678530673951L);
        assertThat(result.getBody()
                         .getEmail()).isEqualTo("test@test.com");
        assertThat(result.getBody()
                         .getId()).isEqualTo(1);
        assertThat(result.getBody()
                         .getNickname()).isEqualTo("test");
        assertThat(result.getBody()
                         .getStatus()).isEqualTo(UserStatus.ACTIVE);

    }

    @DisplayName("email을 통해 내정보를 찾지 못하면 Exception이 발생한다.")
    @Test
    void meExceptionTest() {
        TestContainer testContainer = TestContainer.builder()
                                                   .clockHolder(new TestClockHolder(1678530673951L))
                                                   .build();
        assertThatThrownBy(() -> {
            testContainer.userController.getMyInfo("test@test.com");
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @DisplayName("사용자 정보를 수정한다.")
    @Test
    void updateUserTest() {
        TestContainer testContainer = TestContainer.builder()
                                                   .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                                                   .clockHolder(new TestClockHolder(1678530673951L))
                                                   .build();
        testContainer.userRepository.save(User.builder()
                                              .id(1L)
                                              .email("test@test.com")
                                              .nickname("test")
                                              .status(UserStatus.ACTIVE)
                                              .address("seoul")
                                              .build());
        testContainer.userController.updateMyInfo("test@test.com", UserUpdate.builder()
                                                                             .address("busan")
                                                                             .nickname("test1234")
                                                                             .build());

        ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("test@test.com");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().getNickname()).isEqualTo("test1234");
        assertThat(result.getBody().getAddress()).isEqualTo("busan");
    }

}