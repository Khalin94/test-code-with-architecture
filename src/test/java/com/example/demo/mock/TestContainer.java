package com.example.demo.mock;

import com.example.demo.common.service.ClockHolder;
import com.example.demo.common.service.UuidHolder;
import com.example.demo.post.controller.PostController;
import com.example.demo.post.controller.PostCreateController;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.service.PostServiceImpl;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.controller.UserController;
import com.example.demo.user.controller.UserCreateController;
import com.example.demo.user.controller.port.AuthenticationService;
import com.example.demo.user.controller.port.UserCreateService;
import com.example.demo.user.controller.port.UserReadService;
import com.example.demo.user.controller.port.UserUpdateService;
import com.example.demo.user.service.CertificationService;
import com.example.demo.user.service.UserServiceImpl;
import com.example.demo.user.service.port.MailSender;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;

public class TestContainer {

    public UserReadService userReadService;
    public UserCreateService userCreateService;
    public UserUpdateService userUpdateService;
    public AuthenticationService authenticationService;
    public UserRepository userRepository;
    public CertificationService certificationService;

    public UserController userController;
    public UserCreateController userCreateController;

    private MailSender mailSender;

    public PostRepository postRepository;
    public PostService postService;
    public PostController postController;
    public PostCreateController postCreateController;

    @Builder
    public TestContainer(ClockHolder clockHolder, UuidHolder uuidHolder) {
        this.userRepository = new FakeUserRepository();
        this.mailSender = new FakeMailSender();
        this.certificationService = new CertificationService(this.mailSender);
        UserServiceImpl userService = UserServiceImpl.builder()
                                                     .certificationService(this.certificationService)
                                                     .clockHolder(clockHolder)
                                                     .uuidHolder(uuidHolder)
//                                                     .clockHolder(new TestClockHolder(1678530673951L))
//                                                     .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                                                     .userRepository(this.userRepository)
                                                     .build();
        this.userReadService = userService;
        this.userCreateService = userService;
        this.userUpdateService = userService;
        this.authenticationService = userService;
        this.userController = UserController.builder()
                                            .userReadService(this.userReadService)
                                            .authenticationService(this.authenticationService)
                                            .userUpdateService(this.userUpdateService)
                                            .build();
        this.userCreateController = UserCreateController.builder()
                                                        .userCreateService(this.userCreateService)
                                                        .build();

        this.postRepository = new FakePostRepository();
        this.postService = PostServiceImpl.builder()
                                          .clockHolder(clockHolder)
                                          .postRepository(this.postRepository)
                                          .userRepository(this.userRepository)
                                          .build();
        this.postController = PostController.builder()
                                            .postService(this.postService)
                                            .build();
        this.postCreateController = PostCreateController.builder()
                                                        .postService(this.postService)
                                                        .build();


    }
}
