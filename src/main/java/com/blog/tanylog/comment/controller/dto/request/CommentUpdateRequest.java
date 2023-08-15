package com.blog.tanylog.comment.controller.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentUpdateRequest {

  @NotBlank(message = "내용은 비울 수 없습니다.")
  private String content;

  @Builder
  public CommentUpdateRequest(String content) {
    this.content = content;
  }
}
