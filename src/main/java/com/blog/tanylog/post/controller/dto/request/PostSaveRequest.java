package com.blog.tanylog.post.controller.dto.request;

import com.blog.tanylog.post.domain.Post;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSaveRequest {

  @NotBlank(message = "제목을 비울 수 없습니다.")
  private String title;

  @NotBlank(message = "내용을 비울 수 없습니다.")
  private String content;

  private boolean isDeleted;

  public Post toEntity(String title, String content, boolean isDeleted) {
    return Post.builder()
        .title(title)
        .content(content)
        .isDeleted(isDeleted)
        .build();
  }
}
