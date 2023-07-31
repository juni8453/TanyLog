package com.blog.tanylog.global.exception.controller;

import com.blog.tanylog.global.exception.controller.dto.ExceptionResponse;
import com.blog.tanylog.global.exception.domain.GlobalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

  @ExceptionHandler(GlobalException.class)
  public ResponseEntity<ExceptionResponse> global(GlobalException exception) {

    int statusCode = exception.getStatusCode();

    ExceptionResponse exceptionBody = ExceptionResponse.builder()
        .code(statusCode)
        .message(exception.getMessage())
        .build();

    return ResponseEntity.status(statusCode).body(exceptionBody);
  }
}
