package com.example.demo.post.service;

import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.mock.*;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.CertificationService;
import com.example.demo.user.service.UserService;
import com.example.demo.user.service.port.MailSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostServiceTest {

    private PostService postService;

    @BeforeEach
    public void init() {

        /*MailSender mailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();

        this.userService = UserService.builder()
                                      .certificationService(new CertificationService(mailSender))
                                      .clockHolder(new TestClockHolder(169830L))
                                      .userRepository(fakeUserRepository)
                                      .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
                                      .build();

        fakeUserRepository.save(User.builder()
                                    .id(1L)
                                    .email("test@test.com")
                                    .address("seoul")
                                    .nickname("test")
                                    .status(UserStatus.ACTIVE)
                                    .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                                    .lastLoginAt(0L)
                                    .build());

        fakeUserRepository.save(User.builder()
                                    .id(2L)
                                    .email("test2@test.com")
                                    .address("seoul")
                                    .nickname("test")
                                    .status(UserStatus.PENDING)
                                    .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                                    .lastLoginAt(0L)
                                    .build());*/

        FakePostRepository fakePostRepository = new FakePostRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        User user1 = fakeUserRepository.save(User.builder()
                                                .id(1L)
                                                .email("kok202@naver.com")
                                                .address("Seoul")
                                                .nickname("kok202")
                                                .status(UserStatus.ACTIVE)
                                                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                                                .lastLoginAt(0L)
                                                .build());


        User user2 = fakeUserRepository.save(User.builder()
                                                .id(2L)
                                                .email("kok303@naver.com")
                                                .address("Seoul")
                                                .nickname("kok303")
                                                .status(UserStatus.PENDING)
                                                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                                                .lastLoginAt(0L)
                                                .build());
        this.postService = PostService.builder()
                                      .postRepository(fakePostRepository)
                                      .userRepository(fakeUserRepository)
                                      .clockHolder(new TestClockHolder(1678530673951L))
                                      .build();

        fakePostRepository.save(Post.builder()
                                    .id(1L)
                                    .modifiedAt(0L)
                                    .createdAt(1678530673958L)
                                    .writer(user1)
                                    .content("helloworld")
                                    .build());

    }

    @DisplayName("id를 통해 post 데이터를 가져올 수 있다.")
    @Test
    void getByIdTest() {
        //given
        //when
        Post post = postService.getById(1L);

        //then
        assertThat(post.getContent()).isEqualTo("helloworld");
    }

    @DisplayName("post에 해당 id가 없으면 exception이 발생한다.")
    @Test
    void getByIdExceptionTest() {
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
    void createTest() {
        //given
        PostCreate postCreate = PostCreate.builder()
                                          .content("loren ipsum")
                                          .writerId(1L)
                                          .build();
        Post post = postService.create(postCreate);

        assertThat(post.getContent()).isEqualTo("loren ipsum");
        assertThat(post.getWriter().getId()).isEqualTo(1L);
        assertThat(post.getCreatedAt()).isEqualTo(1678530673951L);
    }

    @DisplayName("PostUpdateDto를 통해 Post를 수정할 수 있다.")
    @Test
    void updateTest() {
        PostUpdate postUpdate = PostUpdate.builder()
                                          .content("test lorem ipsum")
                                          .build();

        postService.update(1L, postUpdate);

        Post post = postService.getById(1L);

        assertThat(post.getContent()).isEqualTo("test lorem ipsum");
        assertThat(post.getModifiedAt()).isEqualTo(1678530673951L);

    }


}