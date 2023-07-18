package com.example.demo.user.controller.domain;

import com.example.demo.user.controller.reponse.MyProfileResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MyProfileResponseTest {

    @Test
    void createMyProfileResponse(){
        User user = User.builder()
                        .id(1L)
                        .email("test@test.com")
                        .address("seoul")
                        .nickname("test")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("aaaaa-bbbbbb-ccccc")
                        .build();

        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

        assertThat(myProfileResponse.getId()).isEqualTo(1L);
        assertThat(myProfileResponse.getEmail()).isEqualTo("test@test.com");
        assertThat(myProfileResponse.getAddress()).isEqualTo("seoul");
        assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
}
