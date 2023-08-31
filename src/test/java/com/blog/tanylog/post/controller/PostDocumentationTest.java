//package com.blog.tanylog.post.controller;
//
//import static org.mockito.ArgumentMatchers.any;
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
//import com.blog.tanylog.config.TestSecurityConfig;
//import com.blog.tanylog.config.WithMockCustomUser;
//import com.blog.tanylog.config.security.UserContext;
//import com.blog.tanylog.post.controller.dto.request.PageSearch;
//import com.blog.tanylog.post.controller.dto.request.PostSaveRequest;
//import com.blog.tanylog.post.controller.dto.request.PostUpdateRequest;
//import com.blog.tanylog.post.controller.dto.response.PostMultiReadResponse;
//import com.blog.tanylog.post.controller.dto.response.PostSingleReadResponse;
//import com.blog.tanylog.post.controller.dto.response.PostWriterResponse;
//import com.blog.tanylog.post.service.PostService;
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
//@WebMvcTest(PostController.class)
//@Import({TestSecurityConfig.class})
//public class PostDocumentationTest {
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
//  @DisplayName("게시글 전체 조회 API")
//  @WithMockCustomUser
//  void 게시글_전체_조회() throws Exception {
//    List<PostSingleReadResponse> singleReadResponses = IntStream.range(0, 20)
//        .mapToObj(i -> PostSingleReadResponse.builder()
//            .id(i + 1L)
//            .title("title - " + i)
//            .content("content - " + i)
//            .createdDate(LocalDateTime.now())
//            .modifiedDate(LocalDateTime.now())
//            .writer(PostWriterResponse.builder().build())
//            .isLiked(false)
//            .build()).collect(Collectors.toList());
//
//    PostMultiReadResponse response = PostMultiReadResponse.builder()
//        .postsResponse(singleReadResponses)
//        .build();
//
//    given(postService.readAll(any(PageSearch.class))).willReturn(response);
//
//    mockMvc.perform(get("/posts")
//            .with(csrf())
//            .accept(MediaType.APPLICATION_JSON))
//        .andDo(print())
//        .andExpect(status().isOk())
//
//        .andDo(document("read_all_posts",
//            responseFields(
//                fieldWithPath("postsResponse").description("List of posts"),
//                fieldWithPath("postsResponse[].id").description("게시글 아이디"),
//                fieldWithPath("postsResponse[].title").description("게시글 제목"),
//                fieldWithPath("postsResponse[].content").description("게시글 내용"),
//                fieldWithPath("postsResponse[].createdDate").description("게시글 작성일"),
//                fieldWithPath("postsResponse[].modifiedDate").description("게시글 수정일"),
//                fieldWithPath("postsResponse[].liked").description("좋아요 여부"),
//                fieldWithPath("postsResponse[].writer").description("게시글 작성자 정보"),
//                fieldWithPath("postsResponse[].writer.name").description("게시글 작성자 이름"),
//                fieldWithPath("postsResponse[].writer.email").description("게시글 작성자 이메일"),
//                fieldWithPath("postsResponse[].writer.picture").description("게시글 작성자 프로필 사진")
//            )));
//  }
//
//  @Test
//  @DisplayName("게시글 단건 조회 API")
//  @WithMockCustomUser
//  void 게시글_단건_조회() throws Exception {
//    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication()
//        .getPrincipal();
//
//    Long postId = 1L;
//
//    PostSingleReadResponse response = PostSingleReadResponse.builder()
//        .id(1L)
//        .title("title")
//        .content("content")
//        .createdDate(LocalDateTime.now())
//        .modifiedDate(LocalDateTime.now())
//        .writer(PostWriterResponse.builder().build())
//        .build();
//
//    given(postService.read(postId, userContext)).willReturn(response);
//
//    mockMvc.perform(get("/posts/{postId}", postId)
//            .with(csrf())
//            .accept(MediaType.APPLICATION_JSON))
//        .andDo(print())
//        .andExpect(status().isOk())
//
//        .andDo(document("read_post",
//            responseFields(
//                fieldWithPath("id").description("게시글 번호"),
//                fieldWithPath("title").description("게시글 제목"),
//                fieldWithPath("content").description("게시글 내용"),
//                fieldWithPath("createdDate").description("게시글 작성일"),
//                fieldWithPath("modifiedDate").description("게시글 수정일"),
//                fieldWithPath("liked").description("좋아요 여부"),
//                fieldWithPath("writer").description("게시글 작성자 정보"),
//                fieldWithPath("writer.name").description("게시글 작성자 이름"),
//                fieldWithPath("writer.email").description("게시글 작성자 이메일"),
//                fieldWithPath("writer.picture").description("게시글 작성자 프로필 사진")
//            )));
//  }
//
//  @Test
//  @DisplayName("자신이 작성한 게시글 전체 조회 API")
//  @WithMockCustomUser
//  void 자신이_작성한_게시글_전체_조회() throws Exception {
//    List<PostSingleReadResponse> singleReadResponses = IntStream.range(0, 20)
//        .mapToObj(i -> PostSingleReadResponse.builder()
//            .id(i + 1L)
//            .title("title - " + i)
//            .content("content - " + i)
//            .createdDate(LocalDateTime.now())
//            .modifiedDate(LocalDateTime.now())
//            .writer(PostWriterResponse.builder().build())
//            .isLiked(false)
//            .build()).collect(Collectors.toList());
//
//    PostMultiReadResponse response = PostMultiReadResponse.builder()
//        .postsResponse(singleReadResponses)
//        .build();
//
//    given(postService.readMyPosts(any(PageSearch.class), any(UserContext.class))).willReturn(response);
//
//    mockMvc.perform(get("/posts/my_posts")
//            .with(csrf())
//            .accept(MediaType.APPLICATION_JSON))
//        .andDo(print())
//        .andExpect(status().isOk())
//
//        .andDo(document("read_my_posts",
//            responseFields(
//                fieldWithPath("postsResponse").description("List of posts"),
//                fieldWithPath("postsResponse[].id").description("게시글 아이디"),
//                fieldWithPath("postsResponse[].title").description("게시글 제목"),
//                fieldWithPath("postsResponse[].content").description("게시글 내용"),
//                fieldWithPath("postsResponse[].createdDate").description("게시글 작성일"),
//                fieldWithPath("postsResponse[].modifiedDate").description("게시글 수정일"),
//                fieldWithPath("postsResponse[].liked").description("좋아요 여부"),
//                fieldWithPath("postsResponse[].writer").description("게시글 작성자 정보"),
//                fieldWithPath("postsResponse[].writer.name").description("게시글 작성자 이름"),
//                fieldWithPath("postsResponse[].writer.email").description("게시글 작성자 이메일"),
//                fieldWithPath("postsResponse[].writer.picture").description("게시글 작성자 프로필 사진")
//            )));
//  }
//
//  @Test
//  @DisplayName("게시글 등록 API")
//  @WithMockCustomUser
//  void 게시글_등록() throws Exception {
//    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication()
//        .getPrincipal();
//
//    PostSaveRequest request = PostSaveRequest.builder()
//        .title("title")
//        .content("content")
//        .isDeleted(false)
//        .build();
//
//    given(postService.save(userContext, request)).willReturn(1L);
//
//    mockMvc.perform(post("/posts")
//            .with(csrf())
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(mapper.writeValueAsString(request))
//            .accept(MediaType.APPLICATION_JSON))
//        .andDo(print())
//        .andExpect(status().isCreated())
//
//        .andDo(document("create_post",
//            requestFields(
//                fieldWithPath("title").description("게시글 제목"),
//                fieldWithPath("content").description("게시글 내용"),
//                fieldWithPath("deleted").description("게시글 삭제 여부")
//            )));
//  }
//
//  @Test
//  @DisplayName("게시글 삭제 API")
//  @WithMockCustomUser
//  void 게시글_삭제() throws Exception {
//    Long postId = 1L;
//
//    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication()
//        .getPrincipal();
//
//    willDoNothing().given(postService).delete(postId, userContext);
//
//    mockMvc.perform(delete("/posts/{postId}", postId)
//            .with(csrf()))
//        .andDo(print())
//        .andExpect(status().isOk())
//
//        .andDo(document("delete_post"));
//  }
//
//  @Test
//  @DisplayName("게시글 수정 API")
//  @WithMockCustomUser
//  void 게시글_수정() throws Exception {
//    Long postId = 1L;
//
//    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication()
//        .getPrincipal();
//
//    PostUpdateRequest request = PostUpdateRequest.builder()
//        .title("update title")
//        .content("update content")
//        .build();
//
//    willDoNothing().given(postService).update(postId, userContext, request);
//
//    mockMvc.perform(put("/posts/{postId}", postId)
//            .with(csrf())
//            .content(mapper.writeValueAsString(request))
//            .contentType(MediaType.APPLICATION_JSON))
//        .andDo(print())
//        .andExpect(status().isOk())
//
//        .andDo(document("update_post",
//            requestFields(
//                fieldWithPath("title").description("게시글 제목"),
//                fieldWithPath("content").description("게시글 내용")
//            )
//        ));
//  }
//}
