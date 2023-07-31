package com.blog.tanylog.global.exception.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionResponse {

  private final int code;
  private final String message;

  @Builder
  public ExceptionResponse(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
