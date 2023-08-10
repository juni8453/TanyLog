package com.blog.tanylog.post.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blog.tanylog.config.TestSecurityConfig;
import com.blog.tanylog.config.WithMockCustomUser;
import com.blog.tanylog.post.controller.dto.request.PostSaveRequest;
import com.blog.tanylog.post.controller.dto.request.PostUpdateRequest;
import com.blog.tanylog.post.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostController.class)
@Import({TestSecurityConfig.class})
class PostControllerTest {

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PostService postService;

  @Test
  @DisplayName("비회원 상태로 게시글을 등록할 수는 없습니다.")
  void 비회원_게시글_등록_Redirect() throws Exception {
    // when, then
    mockMvc.perform(post("/posts")
            .with(csrf()))
        .andDo(print())
        .andExpect(status().is3xxRedirection());
  }

  @Test
  @DisplayName("비회원 상태로 게시글을 삭제할 수는 없습니다.")
  void 비회원_게시글_삭제_Redirect() throws Exception {
    Long postId = 1L;

    mockMvc.perform(delete("/posts/{postId}", postId)
            .with(csrf()))
        .andDo(print())
        .andExpect(status().is3xxRedirection());
  }

  @Test
  @DisplayName("비회원 상태로 게시글을 수정할 수는 없습니다.")
  void 비회원_게시글_수정_Redirect() throws Exception {
    Long postId = 1L;

    mockMvc.perform(put("/posts/{postId}", postId)
            .with(csrf()))
        .andDo(print())
        .andExpect(status().is3xxRedirection());
  }

  @Test
  @DisplayName("게시글 등록 시 제목을 비울 수 없습니다.")
  @WithMockCustomUser
  void 게시글_등록_제목_유효성_검사() throws Exception {
    // given
    PostSaveRequest request = PostSaveRequest.builder()
        .title("")
        .content("test content")
        .isDeleted(false)
        .build();

    // when, then
    mockMvc.perform(post("/posts")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("게시글 등록 시 제목 길이는 50 글자를 넘을 수 없습니다.")
  @WithMockCustomUser
  void 게시글_등록_제목_초과_검사() throws Exception {
    // given
    String title = "";

    for (int i = 0; i < 55; i++) {
      title = title.concat("i");
    }

    PostSaveRequest request = PostSaveRequest.builder()
        .title(title)
        .content("test content")
        .isDeleted(false)
        .build();

    // when, then
    mockMvc.perform(post("/posts")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("게시글 등록 시 내용을 비울 수 없습니다.")
  @WithMockCustomUser
  void 게시글_등록_내용_유효성_검사() throws Exception {
    // given
    PostSaveRequest request = PostSaveRequest.builder()
        .title("test title")
        .content("")
        .isDeleted(false)
        .build();

    // when, then
    mockMvc.perform(post("/posts")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("게시글 수정 시 제목을 비울 수 없습니다.")
  @WithMockCustomUser
  void 게시글_수정_제목_유효성_검사() throws Exception {
    // given
    Long postId = 1L;

    PostUpdateRequest request = PostUpdateRequest.builder()
        .title("")
        .content("update content")
        .build();

    // when, then
    mockMvc.perform(put("/posts/{postId}", postId)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("게시글 수정 시 제목 길이는 50 글자를 넘을 수 없습니다.")
  @WithMockCustomUser
  void 게시글_수정_제목_초과_검사() throws Exception {
    // given
    Long postId = 1L;
    String title = "";

    for (int i = 0; i < 55; i++) {
      title = title.concat("i");
    }

    PostUpdateRequest request = PostUpdateRequest.builder()
        .title(title)
        .content("test content")
        .build();

    // when, then
    mockMvc.perform(put("/posts/{postId}", postId)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("게시글 수정 시 내용을 비울 수 없습니다.")
  @WithMockCustomUser
  void 게시글_수정_내용_유효성_검사() throws Exception {
    // given
    Long postId = 1L;

    PostUpdateRequest request = PostUpdateRequest.builder()
        .title("update title")
        .content("")
        .build();

    // when, then
    mockMvc.perform(put("/posts/{postId}", postId)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }
}
