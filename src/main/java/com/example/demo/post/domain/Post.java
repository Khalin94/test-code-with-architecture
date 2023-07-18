package com.example.demo.post.domain;

import com.example.demo.common.service.ClockHolder;
import com.example.demo.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.Clock;

@Getter
public class Post {

    private Long id;
    private String content;
    private Long createdAt;
    private Long modifiedAt;
    private User writer;

    @Builder
    public Post(Long id, String content, Long createdAt, Long modifiedAt, User writer) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.writer = writer;
    }

    public static Post from(PostCreate postCreate, User writer, ClockHolder clockHolder){
        return Post.builder()
                .content(postCreate.getContent())
                .writer(writer)
                .createdAt(clockHolder.mills())
                .build();
    }

    public Post update(PostUpdate postUpdate, ClockHolder clockHolder){
        return Post.builder()
                   .id(this.id)
                   .content(postUpdate.getContent())
                   .createdAt(this.createdAt)
                   .modifiedAt(clockHolder.mills())
                   .writer(this.writer)
                   .build();
    }
}
