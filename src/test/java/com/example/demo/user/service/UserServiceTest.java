package com.example.demo.user.service;

import com.example.demo.common.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.service.port.MailSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

class UserServiceTest {

    UserServiceImpl userService;

    @BeforeEach
    public void init(){
        MailSender mailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.userService = UserServiceImpl.builder()
                                          .certificationService(new CertificationService(mailSender))
                                          .clockHolder(new TestClockHolder(169830L))
                                          .userRepository(fakeUserRepository)
                                          .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
                                          .build();

        fakeUserRepository.save(User.builder()
                                    .id(1L)
                                    .email("test@test.com")
                                    .address("seoul")
                                    .nickname("test")
                                    .status(UserStatus.ACTIVE)
                                    .certificationCode("bbbb-bbbbb-bbbb")
                                    .lastLoginAt(0L)
                                    .build());

        fakeUserRepository.save(User.builder()
                                    .id(2L)
                                    .email("test2@test.com")
                                    .address("seoul")
                                    .nickname("test")
                                    .status(UserStatus.PENDING)
                                    .certificationCode("bbbb-bbbbb-bbba")
                                    .lastLoginAt(0L)
                                    .build());
    }

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

        //when
        User user = userService.create(userCreate);
        System.out.println("user.getId() = " + user.getId());
        //then
        assertThat(user).isNotNull();
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
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

        assertThat(user.getLastLoginAt()).isEqualTo(169830L);
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