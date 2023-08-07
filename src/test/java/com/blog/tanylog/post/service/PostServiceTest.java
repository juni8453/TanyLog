package com.blog.tanylog.post.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.blog.tanylog.config.WithMockCustomUser;
import com.blog.tanylog.config.security.UserContext;
import com.blog.tanylog.global.exception.domain.UserNotFound;
import com.blog.tanylog.post.controller.dto.request.PostSaveRequest;
import com.blog.tanylog.post.domain.Post;
import com.blog.tanylog.post.repository.PostRepository;
import com.blog.tanylog.user.domain.Role;
import com.blog.tanylog.user.domain.User;
import com.blog.tanylog.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest
class PostServiceTest {

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("USER 권한의 유저는 게시글을 등록할 수 있습니다.")
  @WithMockCustomUser
  void 게시글_생성() {
    // given
    UserContext userContext = (UserContext) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    User loginUser = userRepository.findByOauthId(userContext.getSessionUser().getOauthId())
        .orElseThrow(UserNotFound::new);

    String title = "test title";
    String content = "test content";
    boolean isDeleted = false;
    PostSaveRequest request = PostSaveRequest.builder()
        .title(title)
        .content(content)
        .isDeleted(isDeleted)
        .build();

    Post post = request.toEntity(title, content, isDeleted);
    post.addUser(loginUser);

    // when
    Post savePost = postRepository.save(post);

    // then
    assertThat(savePost).isNotNull();
    assertThat(savePost.getUser().getRole()).isEqualTo(Role.USER);
    assertThat(savePost.getTitle()).isEqualTo(title);
  }
}