package com.blog.tanylog.postLike.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.blog.tanylog.config.DatabaseCleanup;
import com.blog.tanylog.config.WithMockCustomUser;
import com.blog.tanylog.config.security.UserContext;
import com.blog.tanylog.post.domain.Post;
import com.blog.tanylog.post.repository.PostRepository;
import com.blog.tanylog.postLike.controller.dto.request.PostLikeRequest;
import com.blog.tanylog.postLike.domain.PostLike;
import com.blog.tanylog.postLike.repository.PostLikeRepository;
import com.blog.tanylog.user.domain.Role;
import com.blog.tanylog.user.domain.User;
import com.blog.tanylog.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest
class PostLikeServiceTest {

  @Autowired
  private PostLikeService postLikeService;

  @Autowired
  private PostLikeRepository postLikeRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private DatabaseCleanup cleanup;

  @BeforeEach
  void saveDummyData() {
    User user = User.builder()
        .oauthId("dummy_oauthId")
        .name("dummy_user")
        .picture("dummy_picture")
        .email("dummy_email")
        .role(Role.USER).build();

    userRepository.save(user);

    Post post = Post.builder()
        .title("dummy_title")
        .content("dummy_content")
        .isDeleted(false)
        .build();

    post.addUser(user);

    postRepository.save(post);
  }

  @AfterEach
  void clear() {
    cleanup.execute();
  }

  @Test
  @DisplayName("처음 게시글 좋아요를 누르면 좋아요 할 수 있습니다.")
  @WithMockCustomUser
  void 게시글_좋아요_등록() {
    // given
    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    Long userId = userContext.getSessionUser().getUserId();

    Long postId = 1L;

    PostLikeRequest request = PostLikeRequest.builder().isLiked(false).build();

    // when
    postLikeService.saveOrDelete(postId, userContext, request);

    // then
    PostLike postLike = postLikeRepository.findPostLikeByPostIdAndUserId(postId, userId).get();

    assertThat(postLike).isNotNull();
    assertThat(postLike.getUser().getId()).isEqualTo(1L);
    assertThat(postLike.getPost().getId()).isEqualTo(1L);
  }

  @Test
  @DisplayName("이미 좋아요 한 게시글에 좋아요를 누르면 좋아요가 취소됩니다.")
  @WithMockCustomUser
  void 게시글_좋아요_취소() {
    // given
    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    Long userId = userContext.getSessionUser().getUserId();

    Long postId = 1L;

    PostLikeRequest saveRequest = PostLikeRequest.builder().isLiked(false).build();

    PostLikeRequest cancelRequest = PostLikeRequest.builder().isLiked(true).build();

    postLikeService.saveOrDelete(postId, userContext, saveRequest);

    // when
    postLikeService.saveOrDelete(postId, userContext, cancelRequest);

    // then
    PostLike postLike = postLikeRepository.findPostLikeByPostIdAndUserId(postId, userId)
        .orElse(null);

    assertThat(postLike).isNull();
  }
}