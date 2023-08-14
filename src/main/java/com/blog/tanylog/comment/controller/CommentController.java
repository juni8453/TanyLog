package com.blog.tanylog.comment.controller;

import com.blog.tanylog.comment.controller.dto.request.CommentSaveRequest;
import com.blog.tanylog.comment.service.CommentService;
import com.blog.tanylog.config.security.UserContext;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/posts/{postId}/comments")
  public void save(@PathVariable Long postId,
      @AuthenticationPrincipal UserContext userContext,
      @Valid @RequestBody CommentSaveRequest request) {

    commentService.save(postId, userContext, request);
  }

  @PostMapping("/posts/{postId}/comments/{commentId}/reply")
  public void saveReply(@PathVariable Long postId, @PathVariable Long commentId,
      @AuthenticationPrincipal UserContext userContext,
      @Valid @RequestBody CommentSaveRequest request) {

    commentService.saveReply(postId, commentId, userContext, request);
  }

  @DeleteMapping("/comments/{commentId}")
  public void delete(@PathVariable Long commentId,
      @AuthenticationPrincipal UserContext userContext) {

    commentService.delete(commentId, userContext);
  }
}
