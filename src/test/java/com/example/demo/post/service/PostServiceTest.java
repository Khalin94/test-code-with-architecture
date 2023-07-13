package com.example.demo.post.service;

import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.infrastructure.PostEntity;
import com.example.demo.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SqlGroup({
        @Sql(value = "/sql/post-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@TestPropertySource("classpath:test-application.properties")
@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @DisplayName("id를 통해 post 데이터를 가져올 수 있다.")
    @Test
    void getByIdTest() {
        //given
        //when
        PostEntity postEntity = postService.getById(1L);

        //then
        assertThat(postEntity.getContent()).isEqualTo("helloworld");
    }

    @DisplayName("post에 해당 id가 없으면 exception이 발생한다.")
    @Test
    void getByIdExceptionTest(){
//        PostEntity postEntity = postService.getById(2L);
//        assertThat(postEntity.getContent()).isEqualTo("helloworld");

        //given
        //when
        //then
        assertThrows(ResourceNotFoundException.class, () -> {
            postService.getById(2L);
        });

    }

    @DisplayName("PostCreateDto를 통해 Post를 생성할 수 있다.")
    @Test
    void createTest(){
        //given
        PostCreate postCreate = PostCreate.builder()
                                          .content("loren ipsum")
                                          .writerId(1L)
                                          .build();
        PostEntity postEntity = postService.create(postCreate);

        assertThat(postEntity.getContent()).isEqualTo("loren ipsum");
        assertThat(postEntity.getWriter().getId()).isEqualTo(1L);
    }

    @DisplayName("PostUpdateDto를 통해 Post를 수정할 수 있다.")
    @Test
    void updateTest(){
        PostUpdate postUpdate = PostUpdate.builder()
                                          .content("test lorem ipsum")
                                          .build();

        postService.update(1L, postUpdate);

        PostEntity postEntity = postService.getById(1L);

        assertThat(postEntity.getContent()).isEqualTo("test lorem ipsum");
        assertThat(postEntity.getCreatedAt()).isGreaterThan(0L);

    }


}