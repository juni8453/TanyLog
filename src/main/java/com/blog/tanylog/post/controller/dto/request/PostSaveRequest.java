package com.blog.tanylog.post.controller.dto.request;

import com.blog.tanylog.post.domain.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSaveRequest {

  private String title;

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
