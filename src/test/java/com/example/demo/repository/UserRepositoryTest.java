package com.example.demo.repository;

import com.example.demo.model.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("DB 연결 테스트")
    @Test
    void dbConnectionTest() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@test.com");
        userEntity.setAddress("seoul");
        userEntity.setNickname("test");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("bbbbb-bbbbbb-bbbbbbbbb");

        UserEntity result = userRepository.save(userEntity);

        assertThat(result.getId()).isNotNull();
    }

    @DisplayName("findByIdAndStatus를 통해 유저 데이터를 찾아올 수 있다.")
    @Test
    void findUserDataUsingFindByIdAndStatus(){
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@test.com");
        userEntity.setAddress("seoul");
        userEntity.setNickname("test");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("bbbbb-bbbbbb-bbbbbbbbb");

        UserEntity result = userRepository.save(userEntity);
        System.out.println("result = " + result);

        Optional<UserEntity> byIdAndStatus = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

        assertThat(byIdAndStatus.isPresent()).isTrue();

    }

    @DisplayName("findByIdAndStatus는 데이터가 없으면 Optional.empty를 반환한다.")
    @Test
    void ifDataIsEmptyResultIsOptionalEmpty(){
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@test.com");
        userEntity.setAddress("seoul");
        userEntity.setNickname("test");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("bbbbb-bbbbbb-bbbbbbbbb");

        UserEntity result = userRepository.save(userEntity);
        System.out.println("result = " + result);

        Optional<UserEntity> byIdAndStatus = userRepository.findByIdAndStatus(1, UserStatus.PENDING);

        assertThat(byIdAndStatus.isPresent()).isFalse();
        assertThat(byIdAndStatus.isEmpty()).isTrue();

    }

    @DisplayName("findByEmailAndStatus를 통해 유저 데이터를 찾아올 수 있다.")
    @Test
    void findUserDataUsingFindByEmailAndStatus(){
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@test.com");
        userEntity.setAddress("seoul");
        userEntity.setNickname("test");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("bbbbb-bbbbbb-bbbbbbbbb");

        UserEntity result = userRepository.save(userEntity);
        System.out.println("result = " + result);

        Optional<UserEntity> byIdAndStatus = userRepository.findByEmailAndStatus("test@test.com", UserStatus.ACTIVE);

        assertThat(byIdAndStatus.isPresent()).isTrue();

    }

    @DisplayName("findByEmailAndStatus는 데이터가 없으면 Optional.empty를 반환한다.")
    @Test
    void ifDataIsEmptyResultIsOptionalEmptyUsingFindByEmailAndStatus(){
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@test.com");
        userEntity.setAddress("seoul");
        userEntity.setNickname("test");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("bbbbb-bbbbbb-bbbbbbbbb");

        UserEntity result = userRepository.save(userEntity);
        System.out.println("result = " + result);

        Optional<UserEntity> byIdAndStatus = userRepository.findByEmailAndStatus("test@test.com", UserStatus.PENDING);

        assertThat(byIdAndStatus.isPresent()).isFalse();
        assertThat(byIdAndStatus.isEmpty()).isTrue();

    }

}