package com.example.demo.post.service;

import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.common.service.ClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.infrastructure.PostEntity;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.UserService;
import com.example.demo.user.infrastructure.UserEntity;
import java.time.Clock;

import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Builder
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ClockHolder clockHolder;

    // PostService에서 사용하기 때문에 이미 Post의 의미가 내포되어 있음 getPostById -> getById
    public Post getById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    // PostService에서 사용하기 때문에 이미 Post의 의미가 내포되어 있음 createPost -> create
    public Post create(PostCreate postCreate) {
        User user = userRepository.findByIdAndStatus(postCreate.getWriterId(), UserStatus.ACTIVE)
                                  .orElseThrow(() -> new ResourceNotFoundException("Users", postCreate.getWriterId()));
        Post post = Post.from(postCreate, user, clockHolder);
        return postRepository.save(post);
    }

    // PostService에서 사용하기 때문에 이미 Post의 의미가 내포되어 있음 updatePost -> update
    public Post update(long id, PostUpdate postUpdate) {
        Post post = getById(id);
        post = post.update(postUpdate, clockHolder);
        return postRepository.save(post);
    }
}