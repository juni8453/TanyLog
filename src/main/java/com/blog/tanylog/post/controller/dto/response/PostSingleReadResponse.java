package com.blog.tanylog.post.controller.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSingleReadResponse {

  private final Long id;
  private final String title;
  private final String content;
  private final LocalDateTime createdDate;
  private final LocalDateTime modifiedDate;
  private final PostWriterResponse writer;

  @Builder
  public PostSingleReadResponse(Long id, String title, String content,
      LocalDateTime createdDate, LocalDateTime modifiedDate, PostWriterResponse writer) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.createdDate = createdDate;
    this.modifiedDate = modifiedDate;
    this.writer = writer;
  }
}
