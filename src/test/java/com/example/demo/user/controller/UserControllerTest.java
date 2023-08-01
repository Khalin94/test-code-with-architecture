package com.example.demo.user.controller;

import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.user.controller.port.UserReadService;
import com.example.demo.user.controller.port.UserService;
import com.example.demo.user.controller.reponse.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @DisplayName("사용자 정보를 userId로 조회한다.")
    @Test
    void getUserByIdTest() {
        // 자연스럽게 Stub하는 방법과 인터페이스 분리 하는 법
        UserController userController = UserController.builder()
                                                      .userReadService(new UserReadService() {
                                                          @Override
                                                          public User getByEmail(String email) {
                                                              return null;
                                                          }

                                                          @Override
                                                          public User getById(long id) {
                                                              return User.builder()
                                                                         .id(id)
                                                                         .email("test@test.com")
                                                                         .address("seoul")
                                                                         .nickname("test")
                                                                         .status(UserStatus.ACTIVE)
                                                                         .certificationCode("aaaaa-bbbbbb-ccccc")
                                                                         .build();
                                                          }
                                                      })
                                                      .build();

        ResponseEntity<UserResponse> result = userController.getUserById(1);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().getEmail()).isEqualTo("test@test.com");
        assertThat(result.getBody().getNickname()).isEqualTo("test");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);

    }

    @DisplayName("사용자 정보가 없으면 Exception이 발생한다.")
    @Test
    void getUserByIdExceptionTest() {
        // 자연스럽게 Stub하는 방법과 인터페이스 분리 하는 법
        UserController userController = UserController.builder()
                                                      .userReadService(new UserReadService() {
                                                          @Override
                                                          public User getByEmail(String email) {
                                                              return null;
                                                          }

                                                          @Override
                                                          public User getById(long id) {
                                                              throw new ResourceNotFoundException("Users", id);
                                                          }
                                                      })
                                                      .build();

        assertThatThrownBy(() -> {
            userController.getUserById(1);
        }).isInstanceOf(ResourceNotFoundException.class);

    }
//
//    @DisplayName("인증코드로 계정을 활성화 시킬 수 있다.")
//    @Test
//    void verifyEmailTest() throws Exception {
//        mockMvc.perform(get("/api/users/2/verify").queryParam("certificationCode", "bbbb-bbbbb-bbba"))
//               .andExpect(status().isFound())
//        ;
//
//        Optional<UserEntity> userEntity = userJpaRepository.findById(2L);
//
//        assertThat(userEntity.get().getStatus()).isEqualTo(UserStatus.ACTIVE);
//    }
//
//    @Test
//    void verifyEmailExceptionTest() throws Exception {
//        mockMvc.perform(get("/api/users/2/verify").queryParam("certificationCode", "bbbb-bbbbb-bbbg"))
//               .andExpect(status().isForbidden())
//                .andExpect(content().string("자격 증명에 실패하였습니다."));
//    }
//
//    @DisplayName("email을 통해 사용자 정보를 조회한다.")
//    @Test
//    void meTest() throws Exception {
//        mockMvc.perform(get("/api/users/me").header("EMAIL", "test@test.com"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.email").value("test@test.com"))
//                .andExpect(jsonPath("$.nickname").value("test"))
//                .andExpect(jsonPath("$.address").value("seoul"))
//                .andExpect(jsonPath("$.status").value("ACTIVE"))
//               ;
//    }
//
//    @DisplayName("email을 통해 내정보를 찾지 못하면 Exception이 발생한다.")
//    @Test
//    void meExceptionTest() throws Exception {
////        mockMvc.perform(get("/api/users/me").header("EMAIL", "wrong@test.com"))
////                .andExpect(status().isNotFound())
////               ;
//        mockMvc.perform(get("/api/users/me").header("EMAIL", "wrong@test.com"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Users에서 ID wrong@test.com를 찾을 수 없습니다."))
//        ;
//    }
//
//    @DisplayName("사용자 정보를 수정한다.")
//    @Test
//    void updateUserTest() throws Exception {
//        //given
//        UserUpdate userUpdate = UserUpdate.builder()
//                                          .address("kimpo")
//                                          .nickname("hello")
//                                          .build();
//
//        //when
//        //then
//        mockMvc.perform(put("/api/users/me").header("EMAIL", "test@test.com")
//                                            .contentType(MediaType.APPLICATION_JSON)
//                                            .content(objectMapper.writeValueAsString(userUpdate)))
//                .andExpect(jsonPath("$.nickname").value("hello"))
//                .andExpect(jsonPath("$.address").value("kimpo"))
//               ;
//
//    }

}