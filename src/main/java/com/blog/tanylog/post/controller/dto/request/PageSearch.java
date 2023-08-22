package com.blog.tanylog.post.controller.dto.request;

import lombok.Getter;

@Getter
public class PageSearch {

  private static final int MAX_SIZE = 2000;

  private final Long lastRecordId;
  private final Integer page;
  private final Integer size;
  private final String searchType;
  private final String keyword;

  public PageSearch(Long lastRecordId, Integer page, Integer size, String searchType, String keyword) {
    this.lastRecordId = (lastRecordId != null) ? lastRecordId : 1L;
    this.page = (page != null) ? page : 1;
    this.size = (size != null) ? size : 20;
    this.searchType = (searchType != null) ? searchType : "";
    this.keyword = (keyword != null) ? keyword : "";
  }

  // page 가 0인 경우 최소한 1을 반환하기 위함
  // size 가 너무 커지지 않도록 조치
  public long getOffset() {
    return (long) (Math.max(1, page) - 1) * Math.min(MAX_SIZE, size);
  }
}
