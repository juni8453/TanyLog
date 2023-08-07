package com.blog.tanylog.post.controller.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostMultiReadResponse {

  private final List<PostSingleReadResponse> postsResponse;

  @Builder
  public PostMultiReadResponse(List<PostSingleReadResponse> postsResponse) {
    this.postsResponse = postsResponse;
  }
}
