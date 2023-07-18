package com.example.demo.mock;

import com.example.demo.post.domain.Post;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakePostRepository implements PostRepository {

    private long autoIncreaseId = 0L;
    private final List<Post> data = new ArrayList<>();

    @Override
    public Optional<Post> findById(long id) {
        return data.stream()
                   .filter(post -> post.getId() == id)
                   .findAny();
    }

    @Override
    public Post save(Post post) {

        if(post.getId() == null || post.getId() == 0){
            Post newPost = Post.builder()
                               .id(autoIncreaseId++)
                               .createdAt(post.getCreatedAt())
                               .writer(post.getWriter())
                               .content(post.getContent())
                               .modifiedAt(post.getModifiedAt())
                               .build();
            return newPost;
        }else{
            data.removeIf(item -> item.getId().equals(post.getId()));
            data.add(post);
            return post;
        }
    }
}
