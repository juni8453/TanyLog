package com.blog.tanylog.comment.controller.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentMultiReadResponse {

  private final List<CommentSingleReadResponse> commentsResponse;

  @Builder
  public CommentMultiReadResponse(List<CommentSingleReadResponse> commentsResponse) {
    this.commentsResponse = commentsResponse;
  }
}
