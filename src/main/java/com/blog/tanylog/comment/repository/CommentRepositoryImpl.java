package com.blog.tanylog.comment.repository;

import com.blog.tanylog.comment.controller.dto.request.CommentPageSearch;
import com.blog.tanylog.comment.domain.Comment;
import com.blog.tanylog.comment.domain.QComment;
import com.blog.tanylog.post.domain.QPost;
import com.blog.tanylog.user.domain.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentCustomRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<Comment> readNoOffset(Long postId, CommentPageSearch commentPageSearch) {
    Long id = commentPageSearch.getLastRecordId();

    BooleanBuilder dynamicLtId = new BooleanBuilder();

    // 동적 쿼리 설정
    if (id != null) {
      dynamicLtId.and(QComment.comment.id.gt(id));
    }

    return jpaQueryFactory.select(QComment.comment)
        .from(QComment.comment)
        .join(QComment.comment.user, QUser.user).fetchJoin()
        .join(QComment.comment.post, QPost.post).fetchJoin()
        .where(dynamicLtId.and(QComment.comment.isDeleted.eq(false))
            .and(QComment.comment.post.id.eq(postId))
            .and(QComment.comment.replyDepth.eq(0)))
        .orderBy(QComment.comment.id.desc())
        .limit(commentPageSearch.getSize())
        .fetch();
  }
}

