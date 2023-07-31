package com.blog.tanylog.global.exception.controller;

import com.blog.tanylog.global.exception.controller.dto.ExceptionResponse;
import com.blog.tanylog.global.exception.domain.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

  @ExceptionHandler(GlobalException.class)
  public ResponseEntity<ExceptionResponse> global(GlobalException exception) {

    int statusCode = exception.getStatusCode();

    ExceptionResponse response = ExceptionResponse.builder()
        .code(statusCode)
        .message(exception.getMessage())
        .build();

    return ResponseEntity.status(statusCode).body(response);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ExceptionResponse valid(MethodArgumentNotValidException exception) {
    ExceptionResponse response = ExceptionResponse.builder()
        .code(400)
        .message("잘못된 요청입니다.")
        .build();

    for (FieldError fieldError : exception.getFieldErrors()) {
      response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
    }

    return response;
  }
}
