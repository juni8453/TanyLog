package com.blog.tanylog.comment.service;

import com.blog.tanylog.comment.controller.dto.request.CommentPageSearch;
import com.blog.tanylog.comment.controller.dto.request.CommentSaveRequest;
import com.blog.tanylog.comment.controller.dto.request.CommentUpdateRequest;
import com.blog.tanylog.comment.controller.dto.response.CommentMultiReadResponse;
import com.blog.tanylog.comment.controller.dto.response.CommentSingleReadResponse;
import com.blog.tanylog.comment.controller.dto.response.CommentWriterResponse;
import com.blog.tanylog.comment.domain.Comment;
import com.blog.tanylog.comment.repository.CommentRepository;
import com.blog.tanylog.config.security.UserContext;
import com.blog.tanylog.global.exception.domain.CommentDepthOverException;
import com.blog.tanylog.global.exception.domain.CommentNotFound;
import com.blog.tanylog.global.exception.domain.OtherUserDeleteException;
import com.blog.tanylog.global.exception.domain.OtherUserUpdateException;
import com.blog.tanylog.global.exception.domain.PostNotFound;
import com.blog.tanylog.global.exception.domain.UserNotFound;
import com.blog.tanylog.post.domain.Post;
import com.blog.tanylog.post.repository.PostRepository;
import com.blog.tanylog.user.domain.User;
import com.blog.tanylog.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

  private final CommentRepository commentRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;

  @Transactional
  public Long save(Long postId, UserContext userContext, CommentSaveRequest request) {
    Long userId = userContext.getSessionUser().getUserId();
    User loginUser = userRepository.findById(userId)
        .orElseThrow(UserNotFound::new);

    Post findPost = postRepository.findById(postId)
        .orElseThrow(PostNotFound::new);

    String content = request.getContent();
    boolean isDeleted = request.isDeleted();

    Comment comment = request.toEntity(content, isDeleted);
    comment.addUser(loginUser);
    comment.addPost(findPost);

    Comment savedComment = commentRepository.save(comment);

    return savedComment.getId();
  }

  @Transactional
  public void saveReply(Long postId, Long commentId, UserContext userContext,
      CommentSaveRequest request) {
    Long userId = userContext.getSessionUser().getUserId();
    User loginUser = userRepository.findById(userId)
        .orElseThrow(UserNotFound::new);

    Post findPost = postRepository.findById(postId)
        .orElseThrow(PostNotFound::new);

    Comment parentComment = commentRepository.findById(commentId)
        .orElseThrow(CommentNotFound::new);

    if (!parentComment.checkDepth()) {
      throw new CommentDepthOverException();
    }

    String content = request.getContent();
    boolean isDeleted = request.isDeleted();

    Comment replyComment = request.toEntity(content, isDeleted);
    replyComment.addDepth();
    replyComment.addUser(loginUser);
    replyComment.addPost(findPost);
    replyComment.addRelationByComment(parentComment);

    commentRepository.save(replyComment);
  }

  @Transactional
  public void delete(Long commentId, UserContext userContext) {
    Long userId = userContext.getSessionUser().getUserId();
    User loginUser = userRepository.findById(userId)
        .orElseThrow(UserNotFound::new);

    Comment findComment = commentRepository.findById(commentId)
        .orElseThrow(CommentNotFound::new);

    if (!findComment.checkUser(loginUser)) {
      throw new OtherUserDeleteException();
    }

    commentRepository.deleteComment(findComment.getId());
  }

  @Transactional
  public void update(Long commentId, UserContext userContext,
      CommentUpdateRequest request) {
    Long userId = userContext.getSessionUser().getUserId();
    User loginUser = userRepository.findById(userId)
        .orElseThrow(UserNotFound::new);

    Comment findComment = commentRepository.findById(commentId)
        .orElseThrow(CommentNotFound::new);

    if (!findComment.checkUser(loginUser)) {
      throw new OtherUserUpdateException();
    }

    String updateContent = request.getContent();

    findComment.updateComment(updateContent);
  }

  @Transactional
  public CommentMultiReadResponse readAll(Long postId, CommentPageSearch commentPageSearch) {
    List<Comment> comments = getComments(postId, Optional.empty(), commentPageSearch);
    List<CommentSingleReadResponse> response = commentToSingleReadResponse(comments);

    return CommentMultiReadResponse
        .builder()
        .commentsResponse(response)
        .build();
  }

  @Transactional
  public CommentMultiReadResponse readReplyAll(Long postId, Long commentId, CommentPageSearch commentPageSearch) {
    List<Comment> childComments = getComments(postId, Optional.of(commentId), commentPageSearch);
    List<CommentSingleReadResponse> response = commentToSingleReadResponse(childComments);

    return CommentMultiReadResponse
        .builder()
        .commentsResponse(response)
        .build();
  }

  /**
   * @param commentId
   *  comment ID 값이 존재하지 않다면 상위 댓글을 조회합니다.
   *  comment ID 값이 존재한다면 comment ID 의 자식 댓글을 조회합니다.
   */
  private List<Comment> getComments(Long postId, Optional<Long> commentId, CommentPageSearch commentPageSearch) {
    Post findPost = postRepository.findById(postId).orElseThrow(PostNotFound::new);

    if (commentId.isPresent()) {
      return commentRepository.readReplyNoOffset(findPost.getId(), commentId.get(), commentPageSearch);

    } else {
      return commentRepository.readNoOffset(findPost.getId(), commentPageSearch);
    }
  }

  private List<CommentSingleReadResponse> commentToSingleReadResponse(List<Comment> comments) {
    return comments.stream()
        .map(comment -> CommentSingleReadResponse.builder()
            .id(comment.getId())
            .content(comment.getContent())
            .createdDate(comment.getCreateAt())
            .modifiedDate(comment.getModifiedAt())
            .writer(CommentWriterResponse.builder()
                .name(comment.getUser().getName())
                .email(comment.getUser().getEmail())
                .picture(comment.getUser().getPicture())
                .build())
            .build()).collect(Collectors.toList());
  }
}
