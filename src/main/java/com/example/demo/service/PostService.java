package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.dto.PostCreateDto;
import com.example.demo.model.dto.PostUpdateDto;
import com.example.demo.repository.PostEntity;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserEntity;
import java.time.Clock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    // PostService에서 사용하기 때문에 이미 Post의 의미가 내포되어 있음 getPostById -> getById
    public PostEntity getById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    // PostService에서 사용하기 때문에 이미 Post의 의미가 내포되어 있음 createPost -> create
    public PostEntity create(PostCreateDto postCreateDto) {
        UserEntity userEntity = userService.getById(postCreateDto.getWriterId());
        PostEntity postEntity = new PostEntity();
        postEntity.setWriter(userEntity);
        postEntity.setContent(postCreateDto.getContent());
        postEntity.setCreatedAt(Clock.systemUTC().millis());
        return postRepository.save(postEntity);
    }

    // PostService에서 사용하기 때문에 이미 Post의 의미가 내포되어 있음 updatePost -> update
    public PostEntity update(long id, PostUpdateDto postUpdateDto) {
        PostEntity postEntity = getById(id);
        postEntity.setContent(postUpdateDto.getContent());
        postEntity.setModifiedAt(Clock.systemUTC().millis());
        return postRepository.save(postEntity);
    }
}