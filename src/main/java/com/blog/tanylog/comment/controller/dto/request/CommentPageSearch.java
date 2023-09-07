package com.blog.tanylog.comment.controller.dto.request;

import lombok.Getter;

@Getter
public class CommentPageSearch {

  private static final int MAX_SIZE = 2000;

  private final Long lastRecordId;
  private final Integer size;

  public CommentPageSearch(Long lastRecordId, Integer size) {
    this.lastRecordId = (lastRecordId != null) ? lastRecordId : 11L;
    this.size = (size != null) ? size : 10;
  }
}
