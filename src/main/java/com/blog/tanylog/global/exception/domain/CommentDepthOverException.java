package com.blog.tanylog.global.exception.domain;

public class CommentDepthOverException extends GlobalException {

  private static final String MESSAGE = "대댓글에는 대댓글을 작성할 수 없습니다.";

  public CommentDepthOverException() {
    super(MESSAGE);
  }

  @Override
  public int getStatusCode() {
    return 400;
  }

}
