package com.blog.tanylog.postLike.controller;

import com.blog.tanylog.config.security.UserContext;
import com.blog.tanylog.postLike.controller.dto.request.PostLikeSaveRequest;
import com.blog.tanylog.postLike.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PostLikeController {

  private final PostLikeService postLikeService;

  @PostMapping("/posts/{postId}/like")
  public void save(@PathVariable Long postId,
      @AuthenticationPrincipal UserContext userContext,
      @RequestBody PostLikeSaveRequest request) {

    postLikeService.save(postId, userContext, request);
  }
}
