package com.example.demo.post.controller;

import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


class PostControllerTest {

    @DisplayName("게시물 아이디로 게시물을 조회한다.")
    @Test
    void getPostByIdTest() {
        TestContainer testContainer = TestContainer.builder()
                                                   .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                                                   .clockHolder(new TestClockHolder(1678530673951L))
                                                   .build();
        PostController postController = PostController.builder()
                                                      .postService(testContainer.postService)
                                                      .build();

        testContainer.postRepository.save(Post.builder()
                                              .id(2L)
                                              .content("helloworld")
                                              .writer(User.builder()
                                                          .id(1L)
                                                          .email("test@test.com")
                                                          .nickname("test")
                                                          .status(UserStatus.ACTIVE)
                                                          .address("seoul")
                                                          .build())
                                              .build());

        ResponseEntity<PostResponse> result = postController.getPostById(2L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()
                         .getId()).isEqualTo(2L);
        assertThat(result.getBody()
                         .getContent()).isEqualTo("helloworld");
        assertThat(result.getBody()
                         .getWriter()
                         .getEmail()).isEqualTo("test@test.com");

    }

    @DisplayName("없는 게시물을 조히하면 에러가 난다.")
    @Test
    void getPostByIdExceptionTest() {
        TestContainer testContainer = TestContainer.builder()
                                                   .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                                                   .clockHolder(new TestClockHolder(1678530673951L))
                                                   .build();
        assertThatThrownBy(() -> {
            testContainer.postController.getPostById(1L);
        }).isInstanceOf(ResourceNotFoundException.class);


    }

    @DisplayName("게시물을 업데이트 한다.")
    @Test
    void updatePostTest() {
        TestContainer testContainer = TestContainer.builder()
                                                   .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                                                   .clockHolder(new TestClockHolder(1678530673951L))
                                                   .build();
        testContainer.postRepository.save(Post.builder()
                                              .id(2L)
                                              .content("helloworld")
                                              .writer(User.builder()
                                                          .id(1L)
                                                          .email("test@test.com")
                                                          .nickname("test")
                                                          .status(UserStatus.ACTIVE)
                                                          .address("seoul")
                                                          .build())
                                              .build());
        PostUpdate postUpdate = PostUpdate.builder()
                                          .content("apple")
                                          .build();
        ResponseEntity<PostResponse> result = testContainer.postController.updatePost(2L, postUpdate);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().getId()).isEqualTo(2L);
        assertThat(result.getBody().getModifiedAt()).isEqualTo(1678530673951L);
    }


}