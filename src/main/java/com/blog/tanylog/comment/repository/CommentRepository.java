package com.blog.tanylog.comment.repository;

import com.blog.tanylog.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
