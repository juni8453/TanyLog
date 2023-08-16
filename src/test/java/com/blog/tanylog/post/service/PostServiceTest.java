package com.blog.tanylog.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.blog.tanylog.config.DatabaseCleanup;
import com.blog.tanylog.config.WithMockCustomUser;
import com.blog.tanylog.config.security.UserContext;
import com.blog.tanylog.global.exception.domain.OtherUserDeleteException;
import com.blog.tanylog.global.exception.domain.OtherUserUpdateException;
import com.blog.tanylog.post.controller.dto.request.PostSaveRequest;
import com.blog.tanylog.post.controller.dto.request.PostUpdateRequest;
import com.blog.tanylog.post.controller.dto.response.PostSingleReadResponse;
import com.blog.tanylog.post.domain.Post;
import com.blog.tanylog.post.repository.PostRepository;
import com.blog.tanylog.user.domain.Role;
import com.blog.tanylog.user.domain.User;
import com.blog.tanylog.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest
class PostServiceTest {

  @Autowired
  private PostService postService;

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
        .role(Role.USER)
        .build();

    userRepository.save(user);
  }

  @AfterEach
  void clear() {
    cleanup.execute();
  }

  @Test
  @DisplayName("USER 권한의 유저는 게시글을 등록할 수 있습니다.")
  @WithMockCustomUser
  void 게시글_생성() {
    // given
    UserContext userContext = (UserContext) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();

    PostSaveRequest request = PostSaveRequest.builder()
        .title("test title")
        .content("test content")
        .isDeleted(false)
        .build();

    // when
    postService.save(userContext, request);

    // then
    List<Post> findPosts = postRepository.findAll();
    assertThat(findPosts.size()).isEqualTo(1);
  }

  @Test
  @DisplayName("권한에 상관없이 게시글을 조회할 수 있습니다.")
  void 게시글_단일_조회() {
    // given
    User user = userRepository.findById(1L).get();
    Post post = Post.builder()
        .title("test title")
        .content("test content")
        .isDeleted(false)
        .build();

    post.addUser(user);
    postRepository.save(post);

    Long postId = 1L;

    // when
    PostSingleReadResponse response = postService.read(postId);

    // then
    assertThat(response).isNotNull();
    assertThat(response.getTitle()).isEqualTo("test title");
  }

  @Test
  @DisplayName("자신이 작성한 게시글을 삭제할 수 있습니다.")
  @WithMockCustomUser
  void 게시글_삭제() {
    // given
    User user = userRepository.findById(1L).get();
    Post post = Post.builder()
        .title("test title")
        .content("test content")
        .isDeleted(false)
        .build();

    post.addUser(user);
    postRepository.save(post);

    Long postId = 1L;

    UserContext userContext = (UserContext) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();

    // when
    postService.delete(postId, userContext);

    // then
    List<Post> findPosts = postRepository.findAll();
    assertThat(findPosts.get(0).isDeleted()).isEqualTo(true);
  }

  @Test
  @DisplayName("자신이 작성한 게시글을 수정할 수 있습니다.")
  @WithMockCustomUser
  void 게시글_수정() {
    // given
    User user = userRepository.findById(1L).get();
    Post post = Post.builder()
        .title("test title")
        .content("test content")
        .isDeleted(false)
        .build();

    post.addUser(user);
    postRepository.save(post);

    Long postId = 1L;

    UserContext userContext = (UserContext) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();

    PostUpdateRequest request = PostUpdateRequest.builder()
        .title("update title")
        .content("update content")
        .build();

    // when
    postService.update(postId, userContext, request);

    // then
    List<Post> findPosts = postRepository.findAll();
    assertThat(findPosts.get(0).getTitle()).isEqualTo("update title");
    assertThat(findPosts.get(0).getContent()).isEqualTo("update content");
  }

  @Test
  @DisplayName("자신이 작성한 게시글이 아니라면 삭제할 수 없습니다.")
  @WithMockCustomUser(userId = 2)
  void 타인_게시글_삭제() {
    // given
    User user = userRepository.findById(1L).get();
    Post post = Post.builder()
        .title("test title")
        .content("test content")
        .isDeleted(false)
        .build();

    post.addUser(user);
    postRepository.save(post);

    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();

    User loginUser = User.builder()
        .oauthId(userContext.getSessionUser().getOauthId())
        .name(userContext.getSessionUser().getUsername())
        .picture(userContext.getSessionUser().getPicture())
        .email(userContext.getSessionUser().getEmail())
        .role(Role.USER)
        .build();

    userRepository.save(loginUser);

    Long postId = 1L;

    // when, then
    assertThrows(OtherUserDeleteException.class, () -> postService.delete(postId, userContext));
  }

  @Test
  @DisplayName("자신이 작성한 게시글이 아니라면 수정할 수 없습니다.")
  @WithMockCustomUser(userId = 2)
  void 타인_게시글_수정() {
    // given
    User user = userRepository.findById(1L).get();
    Post post = Post.builder()
        .title("test title")
        .content("test content")
        .isDeleted(false)
        .build();

    post.addUser(user);
    postRepository.save(post);

    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();

    User loginUser = User.builder()
        .oauthId(userContext.getSessionUser().getOauthId())
        .name(userContext.getSessionUser().getUsername())
        .picture(userContext.getSessionUser().getPicture())
        .email(userContext.getSessionUser().getEmail())
        .role(Role.USER)
        .build();

    userRepository.save(loginUser);

    Long postId = 1L;

    // when
    assertThrows(OtherUserUpdateException.class,
        () -> postService.update(postId, userContext, PostUpdateRequest.builder().build()));
  }
}