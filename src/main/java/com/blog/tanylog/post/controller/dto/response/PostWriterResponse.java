package com.blog.tanylog.post.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostWriterResponse {

  private final String name;
  private final String email;
  private final String picture;

  @Builder
  public PostWriterResponse(String name, String email, String picture) {
    this.name = name;
    this.email = email;
    this.picture = picture;
  }
}
