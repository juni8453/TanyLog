package com.blog.tanylog.post.controller.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUpdateRequest {

  @Size(max = 50, message = "제목은 50자를 넘길 수 없습니다.")
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
