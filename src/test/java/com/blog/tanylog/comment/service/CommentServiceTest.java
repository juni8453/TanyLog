package com.blog.tanylog.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.blog.tanylog.comment.controller.dto.request.CommentPageSearch;
import com.blog.tanylog.comment.controller.dto.request.CommentSaveRequest;
import com.blog.tanylog.comment.controller.dto.request.CommentUpdateRequest;
import com.blog.tanylog.comment.controller.dto.response.CommentMultiReadResponse;
import com.blog.tanylog.comment.domain.Comment;
import com.blog.tanylog.comment.repository.CommentRepository;
import com.blog.tanylog.config.DatabaseCleanup;
import com.blog.tanylog.config.WithMockCustomUser;
import com.blog.tanylog.config.security.UserContext;
import com.blog.tanylog.global.exception.domain.CommentDepthOverException;
import com.blog.tanylog.global.exception.domain.OtherUserDeleteException;
import com.blog.tanylog.post.domain.Post;
import com.blog.tanylog.post.repository.PostRepository;
import com.blog.tanylog.user.domain.Role;
import com.blog.tanylog.user.domain.User;
import com.blog.tanylog.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
    assertThat(findComments.stream().allMatch(comment -> comment.getReplyDepth() == 0)).isTrue();
  }

  @Test
  @DisplayName("USER 권한의 유저는 대댓글을 등록할 수 있습니다.")
  @WithMockCustomUser
  void 대댓글_등록() {
    // given
    Long postId = 1L;
    Long commentId = 1L;

    Comment comment = commentRepository.save(Comment.builder()
        .content("test content")
        .isDeleted(false)
        .build());

    comment.addUser(userRepository.findById(1L).get());
    comment.addPost(postRepository.findById(1L).get());

    commentRepository.save(comment);

    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();

    CommentSaveRequest request = CommentSaveRequest.builder()
        .content("reply content")
        .isDeleted(false)
        .build();

    // when
    commentService.saveReply(postId, commentId, userContext, request);

    // then
    List<Comment> findComments = commentRepository.findAll();
    assertThat(findComments.size()).isEqualTo(2);
    assertThat(findComments.get(1).getParentComment().getId()).isEqualTo(1L);
    assertThat(findComments.get(1).getReplyDepth()).isEqualTo(1);
  }

  @Test
  @DisplayName("대댓글에는 대댓글을 작성할 수 없습니다.")
  @WithMockCustomUser
  void 무한_대댓글_작성_제한() {
    // given
    Long postId = 1L;
    Long commentId = 1L;

    Comment comment = commentRepository.save(Comment.builder()
        .content("test content")
        .isDeleted(false)
        .build());

    comment.addUser(userRepository.findById(1L).get());
    comment.addPost(postRepository.findById(1L).get());

    commentRepository.save(comment);

    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();

    CommentSaveRequest request = CommentSaveRequest.builder()
        .content("reply content")
        .isDeleted(false)
        .build();

    // when
    commentService.saveReply(postId, commentId, userContext, request);
    List<Comment> comments = commentRepository.findAll();

    // then
    assertThrows(CommentDepthOverException.class,
        () -> commentService.saveReply(postId, comments.get(comments.size() - 1).getId(),
            userContext, request));
  }

  @Test
  @DisplayName("자신이 작성한 댓글을 삭제할 수 있습니다.")
  @WithMockCustomUser
  void 댓글_삭제() {
    // given
    Long commentId = 1L;

    Comment comment = commentRepository.save(Comment.builder()
        .content("test content")
        .isDeleted(false)
        .build());

    comment.addUser(userRepository.findById(1L).get());
    comment.addPost(postRepository.findById(1L).get());

    commentRepository.save(comment);

    UserContext userContext = (UserContext) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();

    // when
    commentService.delete(commentId, userContext);

    // then
    List<Comment> findComments = commentRepository.findAll();
    assertThat(findComments.get(0).isDeleted()).isEqualTo(true);
  }

  @Test
  @DisplayName("자신이 작성한 댓글이 아니라면 삭제할 수 없습니다.")
  @WithMockCustomUser(userId = 2)
  void 타인_댓글_삭제() {
    // given
    Long commentId = 1L;

    Comment comment = commentRepository.save(Comment.builder()
        .content("test content")
        .isDeleted(false)
        .build());

    comment.addUser(userRepository.findById(1L).get());
    comment.addPost(postRepository.findById(1L).get());

    commentRepository.save(comment);

    UserContext userContext = (UserContext) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();

    User loginUser = User.builder()
        .oauthId(userContext.getSessionUser().getOauthId())
        .name(userContext.getSessionUser().getUsername())
        .picture(userContext.getSessionUser().getPicture())
        .email(userContext.getSessionUser().getEmail())
        .role(Role.USER)
        .build();

    userRepository.save(loginUser);

    // when, then
    assertThrows(OtherUserDeleteException.class, () -> commentService.delete(commentId, userContext));
  }

  @Test
  @DisplayName("부모 댓글이 삭제되면, 자식 댓글도 함께 삭제됩니다.")
  @WithMockCustomUser
  void 댓글_삭제시_대댓글_함께_삭제() {
    // given
    Long postId = 1L;
    Long commentId = 1L;

    Comment comment = commentRepository.save(Comment.builder()
        .content("test content")
        .isDeleted(false)
        .build());

    comment.addUser(userRepository.findById(1L).get());
    comment.addPost(postRepository.findById(1L).get());

    commentRepository.save(comment);

    UserContext userContext = (UserContext) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();

    commentService.saveReply(postId, commentId, userContext,
        CommentSaveRequest.builder()
            .content("reply content")
            .isDeleted(false)
            .build());

    // when
    commentService.delete(commentId, userContext);

    // then
    List<Comment> findComments = commentRepository.findAll();
    assertThat(findComments.stream().allMatch(Comment::isDeleted));
  }

  @Test
  @DisplayName("자식 댓글만을 삭제할 수 있습니다.")
  @WithMockCustomUser
  void 대댓글_삭제() {
    // given
    Long postId = 1L;
    Long commentId = 1L;

    Comment comment = commentRepository.save(Comment.builder()
        .content("test content")
        .isDeleted(false)
        .build());

    comment.addUser(userRepository.findById(1L).get());
    comment.addPost(postRepository.findById(1L).get());

    commentRepository.save(comment);

    UserContext userContext = (UserContext) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();

    commentService.saveReply(postId, commentId, userContext,
        CommentSaveRequest.builder()
            .content("reply content")
            .isDeleted(false)
            .build());

    List<Comment> comments = commentRepository.findAll();

    // when
    commentService.delete(comments.get(comments.size() - 1).getId(), userContext);

    // then
    List<Comment> beforeDeleteComments = commentRepository.findAll();
    assertThat(beforeDeleteComments.get(comments.size() - 1).isDeleted()).isEqualTo(true);
  }

  @Test
  @DisplayName("자신이 작성한 댓글을 수정할 수 있습니다.")
  @WithMockCustomUser
  void 댓글_수정() {
    // given
    Long postId = 1L;
    Long commentId = 1L;

    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();

    Comment comment = commentRepository.save(Comment.builder()
        .content("test content")
        .isDeleted(false)
        .build());

    comment.addUser(userRepository.findById(1L).get());
    comment.addPost(postRepository.findById(1L).get());

    commentRepository.save(comment);

    CommentUpdateRequest request = CommentUpdateRequest.builder()
        .content("update content")
        .build();

    // when
    commentService.update(commentId, userContext, request);

    // then
    Comment findComment = commentRepository.findById(1L).get();
    assertThat(findComment.getContent()).isEqualTo("update content");
  }

  @Test
  @DisplayName("상위 댓글 목록을 설정한 개수만큼 내림차순으로 조회합니다.")
  void 댓글_전체_조회() {
    // given
    CommentPageSearch commentPageSearch = new CommentPageSearch(null, 10);

    User user = userRepository.findById(1L).get();
    Post post = postRepository.findById(1L).get();

    List<Comment> comments = IntStream.range(0, 10)
        .mapToObj(i -> Comment.builder()
            .content("content - " + i)
            .isDeleted(false)
            .build())
        .collect(Collectors.toList()).stream().peek(comment -> {
          comment.addUser(user);
          comment.addPost(post);

        }).collect(Collectors.toList());

    commentRepository.saveAll(comments);

    // when
    CommentMultiReadResponse commentMultiReadResponse = commentService.readAll(post.getId(),
        commentPageSearch);

    // then
    assertThat(commentMultiReadResponse.getCommentsResponse().get(0).getId()).isEqualTo(10L);
  }

  @Test
  @DisplayName("상위 댓글의 자식 댓글 목록을 설정한 개수만큼 내림차순으로 조회합니다.")
  void 자식_댓글_전체_조회() {
    // given
    CommentPageSearch commentPageSearch = new CommentPageSearch(null, 10);

    User user = userRepository.findById(1L).get();
    Post post = postRepository.findById(1L).get();

    Comment parentComment = commentRepository.save(Comment.builder()
        .content("parent comment")
        .isDeleted(false)
        .build());

    parentComment.addUser(user);
    parentComment.addPost(post);

    commentRepository.save(parentComment);

    List<Comment> childComments = IntStream.range(0, 10)
        .mapToObj(i -> Comment.builder()
            .content("reply comment - " + i)
            .replyDepth(1)
            .isDeleted(false)
            .build())
        .collect(Collectors.toList()).stream().peek(comment -> {
          comment.addUser(user);
          comment.addPost(post);
          comment.addRelationByComment(parentComment);

        }).collect(Collectors.toList());

    commentRepository.saveAll(childComments);

    // when
    CommentMultiReadResponse commentMultiReadResponse = commentService.readReplyAll(post.getId(),
        parentComment.getId(), commentPageSearch);

    // then
    // 9월 6일에 좀 더 다시 생각
    assertThat(commentMultiReadResponse.getCommentsResponse().size()).isEqualTo(
        commentPageSearch.getSize() - 1);

    assertThat(commentMultiReadResponse.getCommentsResponse().stream()
        .allMatch(commentSingleReadResponse -> commentSingleReadResponse.getContent()
            .contains("reply comment"))).isEqualTo(true);
  }
}
