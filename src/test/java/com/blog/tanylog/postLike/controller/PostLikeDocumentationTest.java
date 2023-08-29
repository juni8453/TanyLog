package com.blog.tanylog.postLike.controller;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blog.tanylog.config.TestSecurityConfig;
import com.blog.tanylog.config.WithMockCustomUser;
import com.blog.tanylog.config.security.UserContext;
import com.blog.tanylog.postLike.controller.dto.request.PostLikeRequest;
import com.blog.tanylog.postLike.service.PostLikeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest(PostLikeController.class)
@Import({TestSecurityConfig.class})
public class PostLikeDocumentationTest {

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PostLikeService postLikeService;

  @Test
  @DisplayName("게시글 좋아요 등록 API")
  @WithMockCustomUser
  void 게시글_좋아요_등록() throws Exception {
    Long postId = 1L;

    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();

    PostLikeRequest request = PostLikeRequest.builder()
        .liked(false)
        .build();

    willDoNothing().given(postLikeService).saveOrDelete(postId, userContext, request);

    mockMvc.perform(post("/posts/{[postId}/like", postId)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())

        .andDo(document("create_post_like",
            requestFields(
                fieldWithPath("liked").description("false 라면 게시글 좋아요 가능")
            )));
  }

  @Test
  @DisplayName("게시글 좋아요 삭제 API")
  @WithMockCustomUser
  void 게시글_좋아요_삭제() throws Exception {
    Long postId = 1L;

    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();

    PostLikeRequest request = PostLikeRequest.builder()
        .liked(true)
        .build();

    willDoNothing().given(postLikeService).saveOrDelete(postId, userContext, request);

    mockMvc.perform(post("/posts/{[postId}/like", postId)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())

        .andDo(document("create_post_like",
            requestFields(
                fieldWithPath("liked").description("true 라면 게시글 좋아요 불가능, 기존 좋아요 삭제")
            )));
  }
}
