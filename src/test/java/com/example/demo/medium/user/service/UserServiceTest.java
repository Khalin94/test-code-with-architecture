package com.example.demo.medium.user.service;

import com.example.demo.common.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.service.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

//@Sql("/sql/user-service-test-data.sql") // sql 테스트 데이터를 만들어 준다.
// 테스트가 여러 개 들어가면 데이터가 남아 있어 첫번째 테스트 외에 error가 발생한다.(각각의 테스트할 때 중복됨)
// 그러므로 @SqlGroup을 사용한다.
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@TestPropertySource("classpath:test-application.properties")
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;
    @MockBean
    private JavaMailSender mailSender;

    @DisplayName("getByEmail은 Active 상태의 유저 데이터만 찾아올 수 있다.")
    @Test
    void getByEmailGetDataOnlyActiveStatus(){

        //given
        String email = "test@test.com";
        //when
        User user = userService.getByEmail(email);

        //then
        assertThat(user.getAddress()).isEqualTo("seoul");
    }

    @DisplayName("getByEmail에 데이터가 없으면 exception이 발생한다.")
    @Test
    void getByEmailHasNoDataThrowException(){
        //given
        String email = "test2@test.com";

        //when
//        UserEntity userEntity = userService.getByEmail(email);

        //then
        // junit
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getByEmail(email);
        });
        // assertj
        assertThatThrownBy(() -> {
            userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);

    }

    @DisplayName("getById는 Active 상태의 유저 데이터만 찾아올 수 있다.")
    @Test
    void getByIdBringDataOnlyActiveStatus(){
        //given
        //when
        User user = userService.getById(1L);

        //then
        assertThat(user.getAddress()).isEqualTo("seoul");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @DisplayName("유저 상태가 Pending이면 exception이 발생한다.")
    @Test
    void getByIdHasNoDataThrowException(){
        //given
        //when
        //then
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getById(2L);
        });
        assertThatThrownBy(() -> {
            userService.getById(2L);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createTest(){
        //given
        UserCreate userCreate = UserCreate.builder()
                                          .email("test3@test.com")
                                          .nickname("test3")
                                          .address("youngdeungpo")
                                          .build();
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class)); // mail 발송 인증이 에러가 나지 않도록 mockito를 이용해 mock 생성

        //when
        User user = userService.create(userCreate);
        //then
        assertThat(user).isNotNull();
        //assertThat(userEntity.getCertificationCode()).isEqualTo(""); //certificationCode는 UUID로 생성하므로 테스트가 안된다... FIXME
    }

    @Test
    void updateTest(){
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                                          .nickname("test4")
                                          .address("busan")
                                          .build();
        //when
        userService.update(1L, userUpdate);

        User user = userService.getById(1L);

        assertThat(user).isNotNull();
        assertThat(user.getNickname()).isEqualTo("test4");
        assertThat(user.getAddress()).isEqualTo("busan");
    }

    @Test
    void loginTest(){

        userService.login(1L);

        User user = userService.getById(1L);

        assertThat(user.getLastLoginAt()).isGreaterThan(0L); // 현재시간을 확인할 수 없어 0보다 클 때로 비고 FIXME
    }

    @DisplayName("id와 certificationCode를 받아서 이메일 인증이 완료되면 status가 Active로 변경된다.")
    @Test
    void verifyEmailTest() {
        //given
        //when
        userService.verifyEmail(2L, "bbbb-bbbbb-bbba");
        User user = userService.getById(2L);

        //then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @DisplayName("이메일 인증이 실패되면 Exception이 발생한다.")
    @Test
    void verifyEmailExceptionTest(){
        //given
        //when
//        userService.verifyEmail(2L, "bbbb-bbbbb-bbbg");
//        UserEntity userEntity = userService.getById(2L);
//        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);

        assertThatThrownBy(() -> {
            userService.verifyEmail(2L, "bbbb-bbbbb-bbbg");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }



}