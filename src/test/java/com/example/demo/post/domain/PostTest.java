package com.example.demo.post.domain;

import com.example.demo.user.domain.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PostTest {

    @Test
    void createPostUsingPostCreate(){
        PostCreate postCreate = PostCreate.builder()
                                          .content("hello world")
                                          .writerId(1L)
                                          .build();
        User writer = User.builder()
                          .email("test@test.com")
                          .address("seoul")
                          .nickname("test")
                          .build();

        Post post = Post.from(postCreate, writer);

        assertThat(post.getContent()).isEqualTo("hello world");
        assertThat(post.getWriter().getEmail()).isEqualTo("test@test.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("test");

    }

}
