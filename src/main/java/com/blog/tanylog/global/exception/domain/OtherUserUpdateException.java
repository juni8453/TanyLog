package com.blog.tanylog.global.exception.domain;

public class OtherUserUpdateException extends GlobalException {

  private static final String MESSAGE = "다른 유저의 게시글은 수정할 수 없습니다.";

  public OtherUserUpdateException() {
    super(MESSAGE);
  }

  @Override
  public int getStatusCode() {
    return 400;
  }
}
