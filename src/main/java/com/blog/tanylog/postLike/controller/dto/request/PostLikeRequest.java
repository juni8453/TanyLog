package com.blog.tanylog.postLike.controller.dto.request;

import com.blog.tanylog.global.exception.domain.BadLikeException;
import com.blog.tanylog.postLike.domain.PostLike;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeRequest {

  @JsonProperty("isLiked")
  private boolean isLiked;

  @Builder
  public PostLikeRequest(boolean isLiked) {
    this.isLiked = isLiked;
  }

  // postLiked != null && isLiked == true 의미 : 이미 좋아요 한 상태임 (삭제만 가능한 상황)
  // postLiked == null && isLiked == false 의미 : 좋아요 안한 상태임 (추가만 가능한 상황)
  public void validate(PostLike postLike) {
    if (postLike != null && !this.isLiked) {
      throw new BadLikeException();
    }

    if (postLike == null && this.isLiked) {
      throw new BadLikeException();
    }
  }
}
