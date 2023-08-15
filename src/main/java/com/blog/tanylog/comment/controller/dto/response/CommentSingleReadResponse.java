package com.blog.tanylog.comment.controller.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentSingleReadResponse {

  private final Long id;
  private final String content;
  private final LocalDateTime createdDate;
  private final LocalDateTime modifiedDate;
  private final CommentWriterResponse writer;

  @Builder
  public CommentSingleReadResponse(Long id,
      CommentWriterResponse writer, LocalDateTime createdDate, LocalDateTime modifiedDate,
      String content) {
    this.id = id;
    this.writer = writer;
    this.createdDate = createdDate;
    this.modifiedDate = modifiedDate;
    this.content = content;
  }
}
