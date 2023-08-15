package com.blog.tanylog.comment.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentWriterResponse {

  private final String name;
  private final String email;
  private final String picture;

  @Builder
  public CommentWriterResponse(String name, String email, String picture) {
    this.name = name;
    this.email = email;
    this.picture = picture;
  }
}

