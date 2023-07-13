package com.example.demo.post.controller;

import com.example.demo.post.domain.PostUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SqlGroup({
        @Sql(value = "/sql/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
})
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SpringBootTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("게시물 아이디로 게시물을 조회한다.")
    @Test
    void getPostByIdTest() throws Exception {

        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("helloworld"))
                .andExpect(jsonPath("$.writer.id").isNumber())
                .andExpect(jsonPath("$.writer.email").value("test@test.com"))
                .andExpect(jsonPath("$.writer.nickname").value("test"))
               ;
    }

    @DisplayName("없는 게시물을 조히하면 에러가 난다.")
    @Test
    void getPostByIdExceptionTest() throws Exception {
        mockMvc.perform(get("/api/posts/100"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Posts에서 ID 100를 찾을 수 없습니다."));

    }

    @DisplayName("게시물을 업데이트 한다.")
    @Test
    void updatePostTest() throws Exception {

        PostUpdate postUpdate = PostUpdate.builder()
                                          .content("updatedContent")
                                          .build();

        mockMvc.perform(put("/api/posts/2").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(postUpdate)))
                .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.content").value("updatedContent"))
                ;

    }




}