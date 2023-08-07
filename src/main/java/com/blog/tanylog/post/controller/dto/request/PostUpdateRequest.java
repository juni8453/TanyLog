package com.blog.tanylog.post.controller.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUpdateRequest {

  @NotBlank(message = "제목을 비울 수 없습니다.")
  private String title;

  @NotBlank(message = "내용을 비울 수 없습니다.")
  private String content;

  @Builder
  public PostUpdateRequest(String title, String content) {
    this.title = title;
    this.content = content;
  }
}
