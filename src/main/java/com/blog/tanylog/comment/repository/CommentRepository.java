package com.blog.tanylog.comment.repository;

import com.blog.tanylog.comment.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  @Query("SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.post")
  List<Comment> findAll();
}
