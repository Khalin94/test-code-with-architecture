package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

//@Sql("/sql/user-service-test-data.sql") // sql 테스트 데이터를 만들어 준다.
// 테스트가 여러 개 들어가면 데이터가 남아 있어 첫번째 테스트 외에 error가 발생한다.(각각의 테스트할 때 중복됨)
// 그러므로 @SqlGroup을 사용한다.
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @DisplayName("getByEmail은 Active 상태의 유저 데이터만 찾아올 수 있다.")
    @Test
    void getByEmailGetDataOnlyActiveStatus(){

        //given
        String email = "test@test.com";
        //when
        UserEntity userEntity = userService.getByEmail(email);

        //then
        assertThat(userEntity.getAddress()).isEqualTo("seoul");
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
        UserEntity userEntity = userService.getById(1L);

        //then
        assertThat(userEntity.getAddress()).isEqualTo("seoul");
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
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




}