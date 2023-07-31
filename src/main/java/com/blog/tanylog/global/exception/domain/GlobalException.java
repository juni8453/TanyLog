package com.blog.tanylog.global.exception.domain;

public abstract class GlobalException extends RuntimeException {

  public GlobalException(String message) {
    super(message);
  }

  public abstract int getStatusCode();
}
