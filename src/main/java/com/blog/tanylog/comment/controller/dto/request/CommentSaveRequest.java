package com.blog.tanylog.comment.controller.dto.request;

import com.blog.tanylog.comment.domain.Comment;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentSaveRequest {

  @NotBlank(message = "내용은 비울 수 없습니다.")
  private String content;

  private boolean isDeleted;

  @Builder
  public CommentSaveRequest(String content, boolean isDeleted) {
    this.content = content;
    this.isDeleted = isDeleted;
  }

  public Comment toEntity(String content, boolean isDeleted) {
    return Comment.builder()
        .content(content)
        .isDeleted(isDeleted)
        .build();
  }
}
