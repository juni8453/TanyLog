package com.blog.tanylog.comment.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.blog.tanylog.comment.controller.dto.request.CommentSaveRequest;
import com.blog.tanylog.comment.domain.Comment;
import com.blog.tanylog.comment.repository.CommentRepository;
import com.blog.tanylog.config.DatabaseCleanup;
import com.blog.tanylog.config.WithMockCustomUser;
import com.blog.tanylog.config.security.UserContext;
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
class CommentServiceTest {

  @Autowired
  private CommentService commentService;

  @Autowired
  private CommentRepository commentRepository;

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
  @DisplayName("USER 권한의 유저는 댓글을 등록할 수 있습니다.")
  @WithMockCustomUser
  void 댓글_등록() {
    // given
    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();

    Long postId = 1L;

    CommentSaveRequest request = CommentSaveRequest.builder()
        .content("test content")
        .isDeleted(false)
        .build();

    // when
    commentService.save(postId, userContext, request);

    // then
    List<Comment> findComments = commentRepository.findAll();
    assertThat(findComments.size()).isEqualTo(1);
    assertThat(findComments.get(0).getPost().getId()).isEqualTo(1L);
    assertThat(findComments.get(0).getPost().getContent()).isEqualTo("dummy_content");
  }
}
