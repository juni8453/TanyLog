package com.blog.tanylog.post.controller.dto.request;

import com.blog.tanylog.post.domain.Post;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSaveRequest {

  @Size(max = 50, message = "제목은 50자를 넘길 수 없습니다.")
  @NotBlank(message = "제목을 비울 수 없습니다.")
  private String title;

  @NotBlank(message = "내용을 비울 수 없습니다.")
  private String content;

  private boolean isDeleted;

  @Builder
  public PostSaveRequest(String title, String content, boolean isDeleted) {
    this.title = title;
    this.content = content;
    this.isDeleted = isDeleted;
  }

  public Post toEntity(String title, String content, boolean isDeleted) {
    return Post.builder()
        .title(title)
        .content(content)
        .isDeleted(isDeleted)
        .build();
  }
}
