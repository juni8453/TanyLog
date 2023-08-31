//package com.blog.tanylog.postLike.controller;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.blog.tanylog.config.TestSecurityConfig;
//import com.blog.tanylog.post.controller.PostController;
//import com.blog.tanylog.post.service.PostService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.web.servlet.MockMvc;
//
//@WebMvcTest(PostController.class)
//@Import({TestSecurityConfig.class})
//class PostLikeControllerTest {
//
//  @Autowired
//  private ObjectMapper mapper;
//
//  @Autowired
//  private MockMvc mockMvc;
//
//  @MockBean
//  private PostService postService;
//
//  @Test
//  @DisplayName("비회원 상태로 게시글 좋아요를 하거나 취소할 수 없습니다.")
//  void 비회원_게시글_좋아요_등록_Redirect() throws Exception {
//    // given
//    Long postId = 1L;
//
//    // when, then
//    mockMvc.perform(post("/posts/{postId}/like", postId)
//            .with(csrf()))
//        .andDo(print())
//        .andExpect(status().is3xxRedirection());
//  }
//}