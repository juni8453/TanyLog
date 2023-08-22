package com.blog.tanylog.global.exception.domain;

public class BadLikeException extends GlobalException {

  private static final String MESSAGE = "잘못된 isLiked 요청입니다.";

  public BadLikeException() {
    super(MESSAGE);
  }

  @Override
  public int getStatusCode() {
    return 400;
  }

}
