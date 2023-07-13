package com.example.demo.user.infrastructure;

import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql("/sql/user-repository-test-data.sql") // sql 테스트 데이터를 만들어 준다.
@DataJpaTest
class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;


    @DisplayName("findByIdAndStatus를 통해 유저 데이터를 찾아올 수 있다.")
    @Test
    void findUserDataUsingFindByIdAndStatus(){

        Optional<UserEntity> byIdAndStatus = userJpaRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

        assertThat(byIdAndStatus.isPresent()).isTrue();

    }

    @DisplayName("findByIdAndStatus는 데이터가 없으면 Optional.empty를 반환한다.")
    @Test
    void ifDataIsEmptyResultIsOptionalEmpty(){

        Optional<UserEntity> byIdAndStatus = userJpaRepository.findByIdAndStatus(1, UserStatus.PENDING);

//        assertThat(byIdAndStatus.isPresent()).isFalse();
        assertThat(byIdAndStatus.isEmpty()).isTrue();

    }

    @DisplayName("findByEmailAndStatus를 통해 유저 데이터를 찾아올 수 있다.")
    @Test
    void findUserDataUsingFindByEmailAndStatus(){

        Optional<UserEntity> byIdAndStatus = userJpaRepository.findByEmailAndStatus("test@test.com", UserStatus.ACTIVE);

        assertThat(byIdAndStatus.isPresent()).isTrue();

    }

    @DisplayName("findByEmailAndStatus는 데이터가 없으면 Optional.empty를 반환한다.")
    @Test
    void ifDataIsEmptyResultIsOptionalEmptyUsingFindByEmailAndStatus(){

        Optional<UserEntity> byIdAndStatus = userJpaRepository.findByEmailAndStatus("test@test.com", UserStatus.PENDING);

        assertThat(byIdAndStatus.isEmpty()).isTrue();

    }

}