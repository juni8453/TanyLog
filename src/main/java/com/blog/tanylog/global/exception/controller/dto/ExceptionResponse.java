package com.blog.tanylog.global.exception.controller.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionResponse {

  private final int code;
  private final String message;
  private final Map<String, String> validation;

  @Builder
  public ExceptionResponse(int code, String message, Map<String, String> validation) {
    this.code = code;
    this.message = message;
    this.validation = validation != null ? validation : new HashMap<>();
  }

  public void addValidation(String filedName, String exceptionMessage) {
    this.validation.put(filedName, exceptionMessage);
  }
}
