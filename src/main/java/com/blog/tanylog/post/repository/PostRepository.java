package com.blog.tanylog.post.repository;

import com.blog.tanylog.post.domain.Post;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

  @Query("SELECT p FROM Post p JOIN FETCH p.user")
  Optional<Post> findById(Long postId);

  @Query(value = "SELECT p FROM Post p JOIN FETCH p.user",
      countQuery = "SELECT COUNT(DISTINCT p) FROM Post p INNER JOIN p.user")
  Page<Post> findAll(Pageable pageable);
}
