package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
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

        Post post = Post.from(postCreate, writer, new TestClockHolder(1678530673951L));

        assertThat(post.getContent()).isEqualTo("hello world");
        assertThat(post.getWriter().getEmail()).isEqualTo("test@test.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("test");

    }

    @Test
    void updatePostUserPostUpdate(){

        PostCreate postCreate = PostCreate.builder()
                                          .content("hello world")
                                          .writerId(1L)
                                          .build();
        User writer = User.builder()
                          .email("test@test.com")
                          .address("seoul")
                          .nickname("test")
                          .build();

        Post post = Post.from(postCreate, writer, new TestClockHolder(1678530673951L));

        PostUpdate postUpdate = PostUpdate.builder()
                                          .content("hello world dlrow olleh")
                                          .build();


        Post newPost = post.update(postUpdate, new TestClockHolder(1678530673951L));

        assertThat(newPost.getContent()).isEqualTo("hello world dlrow olleh");
        assertThat(newPost.getWriter().getEmail()).isEqualTo("test@test.com");
        assertThat(newPost.getWriter().getNickname()).isEqualTo("test");
    }

}
