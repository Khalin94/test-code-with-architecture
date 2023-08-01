package com.example.demo.post.controller;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class PostCreateControllerTest {

    @DisplayName("게시물을 생성한다.")
    @Test
    void createPostTest() {
        TestContainer testContainer = TestContainer.builder()
                                                   .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                                                   .clockHolder(new TestClockHolder(1678530673951L))
                                                   .build();
        testContainer.userRepository.save(User.builder()
                                              .id(1L)
                                              .email("test@test.com")
                                              .nickname("test")
                                              .status(UserStatus.ACTIVE)
                                              .address("seoul")
                                              .build());
        PostCreate postCreate = PostCreate.builder()
                                          .content("helloWorld")
                                          .writerId(1L)
                                          .build();

        ResponseEntity<PostResponse> result = testContainer.postCreateController.createPost(postCreate);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
//        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getContent()).isEqualTo("helloWorld");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(1678530673951L);
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("test@test.com");
        System.out.println(result.getBody().getId());
    }
}
