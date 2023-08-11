package com.blog.tanylog.comment.service;

import com.blog.tanylog.comment.controller.dto.request.CommentSaveRequest;
import com.blog.tanylog.comment.domain.Comment;
import com.blog.tanylog.comment.repository.CommentRepository;
import com.blog.tanylog.config.security.UserContext;
import com.blog.tanylog.global.exception.domain.PostNotFound;
import com.blog.tanylog.global.exception.domain.UserNotFound;
import com.blog.tanylog.post.domain.Post;
import com.blog.tanylog.post.repository.PostRepository;
import com.blog.tanylog.user.domain.User;
import com.blog.tanylog.user.repository.UserRepository;
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

    Post ownerPost = postRepository.findById(postId)
        .orElseThrow(PostNotFound::new);

    String content = request.getContent();
    boolean isDeleted = request.isDeleted();

    Comment comment = request.toEntity(content, isDeleted);
    comment.addUser(loginUser);
    comment.addPost(ownerPost);

    commentRepository.save(comment);
  }
}
