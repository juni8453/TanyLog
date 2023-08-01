package com.blog.tanylog.post.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PostMultiReadResponse {

  private final Page<PostSingleReadResponse> postsResponse;

  @Builder
  public PostMultiReadResponse(Page<PostSingleReadResponse> postsResponse) {
    this.postsResponse = postsResponse;
  }
}
