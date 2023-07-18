package com.example.demo.user.controller.domain;

import com.example.demo.user.controller.reponse.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserResponseTest {

    @Test
    void createUserResponse(){

        UserCreate userCreate = UserCreate.builder()
                                          .email("test@test.com")
                                          .address("seoul")
                                          .nickname("test")
                                          .build();

//        User user = User.from(userCreate);
        User user = User.builder()
                        .id(1L)
                        .email("test@test.com")
                        .address("seoul")
                        .nickname("test")
                        .status(UserStatus.PENDING)
                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                        .build();

        UserResponse userResponse = UserResponse.from(user);

        assertThat(userResponse.getEmail()).isEqualTo("test@test.com");
        assertThat(userResponse.getNickname()).isEqualTo("test");
        assertThat(userResponse.getStatus()).isEqualTo(UserStatus.PENDING);

    }


}
