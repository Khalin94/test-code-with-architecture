package com.example.demo.repository;

import com.example.demo.model.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql("/sql/test-user-data.sql") // sql 테스트 데이터를 만들어 준다.
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @DisplayName("findByIdAndStatus를 통해 유저 데이터를 찾아올 수 있다.")
    @Test
    void findUserDataUsingFindByIdAndStatus(){

        Optional<UserEntity> byIdAndStatus = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

        assertThat(byIdAndStatus.isPresent()).isTrue();

    }

    @DisplayName("findByIdAndStatus는 데이터가 없으면 Optional.empty를 반환한다.")
    @Test
    void ifDataIsEmptyResultIsOptionalEmpty(){

        Optional<UserEntity> byIdAndStatus = userRepository.findByIdAndStatus(1, UserStatus.PENDING);

//        assertThat(byIdAndStatus.isPresent()).isFalse();
        assertThat(byIdAndStatus.isEmpty()).isTrue();

    }

    @DisplayName("findByEmailAndStatus를 통해 유저 데이터를 찾아올 수 있다.")
    @Test
    void findUserDataUsingFindByEmailAndStatus(){

        Optional<UserEntity> byIdAndStatus = userRepository.findByEmailAndStatus("test@test.com", UserStatus.ACTIVE);

        assertThat(byIdAndStatus.isPresent()).isTrue();

    }

    @DisplayName("findByEmailAndStatus는 데이터가 없으면 Optional.empty를 반환한다.")
    @Test
    void ifDataIsEmptyResultIsOptionalEmptyUsingFindByEmailAndStatus(){

        Optional<UserEntity> byIdAndStatus = userRepository.findByEmailAndStatus("test@test.com", UserStatus.PENDING);

        assertThat(byIdAndStatus.isEmpty()).isTrue();

    }

}