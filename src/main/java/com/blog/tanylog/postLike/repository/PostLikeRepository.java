package com.blog.tanylog.postLike.repository;

import com.blog.tanylog.postLike.domain.PostLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

  /**
   * 좋아요 객체의 존재 여부만 판단할 때 사용하기 때문에 User, Post 와의 FETCH JOIN 은 사용하지 않습니다.
   */
  @Query("SELECT pl FROM PostLike pl WHERE pl.post.id = :postId AND pl.user.id = :userId")
  Optional<PostLike> findPostLikeByPostIdAndUserId(
      @Param("postId") Long postId,
      @Param("userId") Long userId);
}
