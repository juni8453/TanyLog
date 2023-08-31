//package com.blog.tanylog.comment.controller;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.willDoNothing;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
//import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
//import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.blog.tanylog.comment.controller.dto.request.CommentPageSearch;
//import com.blog.tanylog.comment.controller.dto.request.CommentSaveRequest;
//import com.blog.tanylog.comment.controller.dto.request.CommentUpdateRequest;
//import com.blog.tanylog.comment.controller.dto.response.CommentMultiReadResponse;
//import com.blog.tanylog.comment.controller.dto.response.CommentSingleReadResponse;
//import com.blog.tanylog.comment.controller.dto.response.CommentWriterResponse;
//import com.blog.tanylog.comment.service.CommentService;
//import com.blog.tanylog.config.TestSecurityConfig;
//import com.blog.tanylog.config.WithMockCustomUser;
//import com.blog.tanylog.config.security.UserContext;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.web.servlet.MockMvc;
//
//@AutoConfigureRestDocs
//@WebMvcTest(CommentController.class)
//@Import({TestSecurityConfig.class})
//public class CommentDocumentationTest {
//
//  @Autowired
//  private ObjectMapper mapper;
//
//  @Autowired
//  private MockMvc mockMvc;
//
//  @MockBean
//  private CommentService commentService;
//
//  @Test
//  @DisplayName("댓글 등록 API")
//  @WithMockCustomUser
//  void 댓글_등록() throws Exception {
//    Long postId = 1L;
//
//    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication()
//        .getPrincipal();
//
//    CommentSaveRequest request = CommentSaveRequest.builder()
//        .content("content")
//        .isDeleted(false)
//        .build();
//
//    given(commentService.save(postId, userContext, request)).willReturn(1L);
//
//    mockMvc.perform(post("/posts/{postId}/comments", postId)
//            .with(csrf())
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(mapper.writeValueAsString(request)))
//        .andDo(print())
//        .andExpect(status().isCreated())
//
//        .andDo(document("create_comment",
//            requestFields(
//                fieldWithPath("content").description("댓글 내용"),
//                fieldWithPath("deleted").description("댓글 삭제 여부")
//            )));
//  }
//
//  @Test
//  @DisplayName("대댓글 등록 API")
//  @WithMockCustomUser
//  void 대댓글_등록() throws Exception {
//    Long postId = 1L;
//    Long commentId = 1L;
//
//    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication()
//        .getPrincipal();
//
//    CommentSaveRequest request = CommentSaveRequest.builder()
//        .content("content")
//        .isDeleted(false)
//        .build();
//
//    given(commentService.saveReply(postId, commentId, userContext, request)).willReturn(1L);
//
//    mockMvc.perform(post("/posts/{postId}/comments/{commentId}/reply", postId, commentId)
//            .with(csrf())
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(mapper.writeValueAsString(request)))
//        .andDo(print())
//        .andExpect(status().isCreated())
//
//        .andDo(document("create_reply_comment",
//            requestFields(
//                fieldWithPath("content").description("대댓글 내용"),
//                fieldWithPath("deleted").description("대댓글 삭제 여부")
//            )));
//  }
//
//  @Test
//  @DisplayName("댓글 삭제 API")
//  @WithMockCustomUser
//  void 댓글_삭제() throws Exception {
//    Long commentId = 1L;
//
//    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication()
//        .getPrincipal();
//
//    willDoNothing().given(commentService).delete(commentId, userContext);
//
//    mockMvc.perform(delete("/comments/{commentId}", commentId)
//            .with(csrf()))
//        .andDo(print())
//        .andExpect(status().isOk())
//
//        .andDo(document("delete_comment"));
//  }
//
//  @Test
//  @DisplayName("댓글 수정 API")
//  @WithMockCustomUser
//  void 댓글_수정() throws Exception {
//    Long commentId = 1L;
//
//    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication()
//        .getPrincipal();
//
//    CommentUpdateRequest request = CommentUpdateRequest.builder()
//        .content("update content")
//        .build();
//
//    willDoNothing().given(commentService).update(commentId, userContext, request);
//
//    mockMvc.perform(put("/comments/{commentId}", commentId)
//            .with(csrf())
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(mapper.writeValueAsString(request)))
//        .andDo(print())
//        .andExpect(status().isOk())
//
//        .andDo(document("update_comment",
//            requestFields(
//                fieldWithPath("content").description("댓글 내용")
//            )));
//  }
//
//  @Test
//  @DisplayName("댓글 전체 조회 API")
//  @WithMockCustomUser
//  void 댓글_전체_조회() throws Exception {
//    Long postId = 1L;
//
//    List<CommentSingleReadResponse> singleReadResponses = IntStream.range(0, 20)
//        .mapToObj(i -> CommentSingleReadResponse.builder()
//            .id(i + 1L)
//            .content("content - " + i)
//            .createdDate(LocalDateTime.now())
//            .modifiedDate(LocalDateTime.now())
//            .writer(CommentWriterResponse.builder().build())
//            .build())
//        .collect(Collectors.toList());
//
//    CommentMultiReadResponse response = CommentMultiReadResponse.builder()
//        .commentsResponse(singleReadResponses)
//        .build();
//
//    given(commentService.readAll(anyLong(), any(CommentPageSearch.class))).willReturn(response);
//
//    mockMvc.perform(get("/posts/{postId}/comments", postId)
//            .with(csrf())
//            .accept(MediaType.APPLICATION_JSON))
//        .andDo(print())
//        .andExpect(status().isOk())
//
//        .andDo(document("read_comments",
//            responseFields(
//                fieldWithPath("commentsResponse[].id").description("댓글 ID"),
//                fieldWithPath("commentsResponse[].content").description("댓글 내용"),
//                fieldWithPath("commentsResponse[].createdDate").description("댓글 생성 날짜"),
//                fieldWithPath("commentsResponse[].modifiedDate").description("댓글 수정 날짜"),
//                fieldWithPath("commentsResponse[].writer.name").description("댓글 작성자 이름"),
//                fieldWithPath("commentsResponse[].writer.email").description("댓글 작성자 이메일"),
//                fieldWithPath("commentsResponse[].writer.picture").description("댓글 작성자 프로필 사진 URL")
//            )));
//  }
//
//  @Test
//  @DisplayName("대댓글 전체 조회 API")
//  @WithMockCustomUser
//  void 대댓글_전체_조회() throws Exception {
//    Long postId = 1L;
//    Long commentId = 1L;
//
//    List<CommentSingleReadResponse> singleReadResponses = IntStream.range(0, 20)
//        .mapToObj(i -> CommentSingleReadResponse.builder()
//            .id(i + 1L)
//            .content("reply content - " + i)
//            .createdDate(LocalDateTime.now())
//            .modifiedDate(LocalDateTime.now())
//            .writer(CommentWriterResponse.builder().build())
//            .build())
//        .collect(Collectors.toList());
//
//    CommentMultiReadResponse response = CommentMultiReadResponse.builder()
//        .commentsResponse(singleReadResponses)
//        .build();
//
//    given(
//        commentService.readReplyAll(anyLong(), anyLong(), any(CommentPageSearch.class))).willReturn(
//        response);
//
//    mockMvc.perform(get("/posts/{postId}/comments/{commentId}", postId, commentId)
//            .with(csrf())
//            .accept(MediaType.APPLICATION_JSON))
//        .andDo(print())
//        .andExpect(status().isOk())
//
//        .andDo(document("read_reply_comments",
//            responseFields(
//                fieldWithPath("commentsResponse[].id").description("대댓글 ID"),
//                fieldWithPath("commentsResponse[].content").description("대댓글 내용"),
//                fieldWithPath("commentsResponse[].createdDate").description("대댓글 생성 날짜"),
//                fieldWithPath("commentsResponse[].modifiedDate").description("대댓글 수정 날짜"),
//                fieldWithPath("commentsResponse[].writer.name").description("대댓글 작성자 이름"),
//                fieldWithPath("commentsResponse[].writer.email").description("대댓글 작성자 이메일"),
//                fieldWithPath("commentsResponse[].writer.picture").description("대댓글 작성자 프로필 사진 URL")
//            )));
//  }
//}
