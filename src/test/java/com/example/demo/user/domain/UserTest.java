package com.example.demo.user.domain;

import com.example.demo.common.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class UserTest {

    @Test
    void createUserUsingUserCreateObject(){
        UserCreate userCreate = UserCreate.builder()
                .email("test2@test.com")
                .address("busan")
                .nickname("test2")
                                          .build();

        User user = User.from(userCreate, new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"));

        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("test2@test.com");
        assertThat(user.getNickname()).isEqualTo("test2");
        assertThat(user.getAddress()).isEqualTo("busan");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
    }

    @Test
    void updateUserUsingUserUpdateObject(){

        User user = User.builder()
                        .id(1L)
                        .email("test@test.com")
                        .nickname("test")
                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                        .status(UserStatus.ACTIVE)
                        .lastLoginAt(100L)
                        .address("busan")
                        .build();

        UserUpdate userUpdate = UserUpdate.builder()
                                          .nickname("test1111")
                                          .address("seoul")
                                          .build();

        user = user.update(userUpdate);

        assertThat(user.getEmail()).isEqualTo("test@test.com");
        assertThat(user.getNickname()).isEqualTo("test1111");
        assertThat(user.getAddress()).isEqualTo("seoul");
    }

    @Test
    void loginTest(){
        User user = User.builder()
                        .id(1L)
                        .email("test@test.com")
                        .nickname("test")
                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                        .status(UserStatus.ACTIVE)
                        .lastLoginAt(100L)
                        .address("busan")
                        .build();

        user = user.login(new TestClockHolder(15230245L));

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("test@test.com");
        assertThat(user.getNickname()).isEqualTo("test");
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getLastLoginAt()).isEqualTo(15230245L);
        assertThat(user.getAddress()).isEqualTo("busan");
    }

    @Test
    void userCertificateTest(){
        User user = User.builder()
                        .id(1L)
                        .email("test@test.com")
                        .nickname("test")
                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                        .status(UserStatus.PENDING)
                        .lastLoginAt(100L)
                        .address("busan")
                        .build();

        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);

        user = user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);

    }

    @Test
    void userCertificationExceptionTest(){
        User user = User.builder()
                        .id(1L)
                        .email("test@test.com")
                        .nickname("test")
                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                        .status(UserStatus.PENDING)
                        .lastLoginAt(100L)
                        .address("busan")
                        .build();
        assertThatThrownBy(() ->{
            user.certificate("bbbbbbbb-bbbbb-bbbbb-bbbbbbbb");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);

    }
}
