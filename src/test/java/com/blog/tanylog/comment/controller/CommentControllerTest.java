package com.blog.tanylog.comment.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blog.tanylog.comment.controller.dto.request.CommentSaveRequest;
import com.blog.tanylog.comment.service.CommentService;
import com.blog.tanylog.config.TestSecurityConfig;
import com.blog.tanylog.config.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommentController.class)
@Import({TestSecurityConfig.class})
class CommentControllerTest {

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CommentService commentService;

  @Test
  @DisplayName("비회원 상태로 댓글을 등록할 수는 없습니다.")
  void 비회원_게시글_등록_Redirect() throws Exception {
    // given
    Long postId = 1L;

    // when, then
    mockMvc.perform(post("/posts/{postId}/comments", postId)
            .with(csrf()))
        .andDo(print())
        .andExpect(status().is3xxRedirection());
  }

  @Test
  @DisplayName("댓글 등록 시 내용을 비울 수 없습니다.")
  @WithMockCustomUser
  void 게시글_등록_내용_유효성_검사() throws Exception {
    // given
    Long postId = 1L;

    CommentSaveRequest request = CommentSaveRequest.builder()
        .content("")
        .isDeleted(false)
        .build();

    // when, then
    mockMvc.perform(post("/posts/{postId}/comments", postId)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("비회원 상태로 댓글을 삭제할 수는 없습니다.")
  void 비회원_댓글_삭제_Redirect() throws Exception {
    Long commentId = 1L;

    mockMvc.perform(delete("/comments/{commentId}", commentId)
            .with(csrf()))
        .andDo(print())
        .andExpect(status().is3xxRedirection());
  }
}
