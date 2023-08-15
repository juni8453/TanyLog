package com.blog.tanylog.comment.repository;

import com.blog.tanylog.comment.controller.dto.request.CommentPageSearch;
import com.blog.tanylog.comment.domain.Comment;
import java.util.List;

public interface CommentCustomRepository {

  List<Comment> readNoOffset(Long postId, CommentPageSearch commentPageSearch);
}
