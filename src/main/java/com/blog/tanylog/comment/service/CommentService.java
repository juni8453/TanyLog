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
import com.blog.tanylog.global.exception.domain.PostNotFound;
import com.blog.tanylog.global.exception.domain.UserNotFound;
import com.blog.tanylog.post.domain.Post;
import com.blog.tanylog.post.repository.PostRepository;
import com.blog.tanylog.user.domain.User;
import com.blog.tanylog.user.repository.UserRepository;
import java.util.List;
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
  public void save(Long postId, UserContext userContext, CommentSaveRequest request) {
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

    commentRepository.save(comment);
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
  public void update(Long postId, Long commentId, UserContext userContext,
      CommentUpdateRequest request) {
    Long userId = userContext.getSessionUser().getUserId();
    User loginUser = userRepository.findById(userId)
        .orElseThrow(UserNotFound::new);

    Post findPost = postRepository.findById(postId)
        .orElseThrow(PostNotFound::new);

    if (!findPost.checkUser(loginUser)) {
      throw new OtherUserDeleteException();
    }

    Comment findComment = commentRepository.findById(commentId)
        .orElseThrow(CommentNotFound::new);

    String updateContent = request.getContent();

    findComment.updateComment(updateContent);
  }

  @Transactional
  public CommentMultiReadResponse readAll(Long postId, CommentPageSearch commentPageSearch) {
    // user 필드를 쓰지 않는데 굳이 findByPostId() 대신 findById() 써도 될 듯?
    Post findPost = postRepository.findByPostId(postId).orElseThrow(PostNotFound::new);

    List<Comment> comments = commentRepository.readNoOffset(findPost.getId(), commentPageSearch);

    List<CommentSingleReadResponse> response = comments.stream()
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
            .build())
        .collect(Collectors.toList());

    return CommentMultiReadResponse
        .builder()
        .commentsResponse(response)
        .build();
  }

  public CommentMultiReadResponse readReplyAll(Long postId, Long commentId,
      CommentPageSearch commentPageSearch) {
    Post findPost = postRepository.findByPostId(postId).orElseThrow(PostNotFound::new);

    Comment findComment = commentRepository.findById(commentId).orElseThrow(CommentNotFound::new);

    List<Comment> comments = commentRepository.readReplyNoOffset(findPost.getId(),
        findComment.getId(), commentPageSearch);

    List<CommentSingleReadResponse> response = comments.stream()
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

    return CommentMultiReadResponse
        .builder()
        .commentsResponse(response)
        .build();
  }
}
