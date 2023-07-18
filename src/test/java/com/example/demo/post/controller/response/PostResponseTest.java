package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PostResponseTest {

    @Test
    void createPostResponse(){

        Post post = Post.builder()
                        .id(1L)
                        .content("hello World")
                        .writer(User.builder()
                                    .email("test@test.com")
                                    .nickname("test")
                                    .status(UserStatus.ACTIVE)
                                    .address("seoul")
                                    .build())
                        .createdAt(1678530673958L)
                        .build();

        PostResponse postResponse = PostResponse.from(post);

        assertThat(postResponse.getContent()).isEqualTo("hello World");
        assertThat(postResponse.getWriter().getEmail()).isEqualTo("test@test.com");
        assertThat(postResponse.getId()).isEqualTo(1L);
    }
}
