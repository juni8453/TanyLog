package com.blog.tanylog.post.controller;

import com.blog.tanylog.config.security.UserContext;
import com.blog.tanylog.post.controller.dto.request.PostSaveRequest;
import com.blog.tanylog.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PostController {

  private final PostService postService;

  @PostMapping("/posts")
  public void save(@RequestBody PostSaveRequest request,
      @AuthenticationPrincipal UserContext userContext) {
    postService.save(userContext, request);
  }

  @DeleteMapping("/posts/{postId}")
  public void delete(@PathVariable Long postId, @AuthenticationPrincipal UserContext userContext) {
    postService.delete(postId, userContext);
  }
}
