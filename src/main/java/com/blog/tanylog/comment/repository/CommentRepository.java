package com.blog.tanylog.comment.repository;

import com.blog.tanylog.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  @Modifying(clearAutomatically = true)
  @Query("UPDATE Comment c SET c.isDeleted = true WHERE c.id = :commentId OR c.parentComment.id = :commentId")
  void deleteComment(@Param("commentId") Long commentId);
}
