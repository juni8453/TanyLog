package com.blog.tanylog.post.repository;

import com.blog.tanylog.post.domain.Post;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {

  /**
   * Description
   *  게시글 ID 를 통해 해당 게시글을 조회하면서 어떤 유저가 작성 했는지 함께 조회합니다.
   */
  @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.id = :postId")
  Optional<Post> findByPostId(Long postId);
}
