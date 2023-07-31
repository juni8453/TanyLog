package com.blog.tanylog.post.repository;

import com.blog.tanylog.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
