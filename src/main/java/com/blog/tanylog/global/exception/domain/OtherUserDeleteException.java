package com.blog.tanylog.global.exception.domain;

public class OtherUserDeleteException extends GlobalException {

  private static final String MESSAGE = "다른 유저의 글은 삭제할 수 없습니다.";

  public OtherUserDeleteException() {
    super(MESSAGE);
  }

  @Override
  public int getStatusCode() {
    return 400;
  }
}
