package com.blog.tanylog.postLike.controller.dto.request;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.blog.tanylog.global.exception.domain.BadLikeException;
import com.blog.tanylog.postLike.domain.PostLike;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostLikeRequestTest {

  @Test
  @DisplayName("게시글 좋아요 객체가 있는 상태에서 좋아요 등록 요청이 오면 실패합니다.")
  void 게시글_좋아요_등록_검증() {
    // given
    PostLike postLike = PostLike.createPostLike();

    PostLikeRequest request = PostLikeRequest.builder()
        .isLiked(false)
        .build();

    // when, then
    assertThrows(BadLikeException.class, () -> request.validate(postLike));
  }

  @Test
  @DisplayName("게시글 좋아요 객체가 없는 상태에서 좋아요 삭제 요청이 오면 실패합니다.")
  void 게시글_좋야요_삭제_검증() {
    // given
    PostLike postLike = null;

    PostLikeRequest request = PostLikeRequest.builder()
        .isLiked(true)
        .build();

    // when, then
    assertThrows(BadLikeException.class, () -> request.validate(postLike));
  }
}