package com.blog.tanylog.post.repository;

import com.blog.tanylog.post.controller.dto.request.PageSearch;
import com.blog.tanylog.post.domain.Post;
import com.blog.tanylog.post.domain.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostCustomRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<Post> readAll(PageSearch pageSearch) {
    return jpaQueryFactory.selectFrom(QPost.post)
        .join(QPost.post.user)
        .fetchJoin()
        .where(QPost.post.isDeleted.eq(false))
        .limit(pageSearch.getSize())
        .offset(pageSearch.getOffset())
        .orderBy(QPost.post.id.desc())
        .fetch();
  }

  @Override
  public List<Post> readNoOffset(PageSearch pageSearch) {
    Long id = pageSearch.getLastRecordId();

    BooleanBuilder dynamicLtId = new BooleanBuilder();

    // 동적 쿼리 설정
    if (id != null) {
      dynamicLtId.and(QPost.post.id.lt(id));
    }

    return jpaQueryFactory.select(QPost.post)
        .from(QPost.post)
        .join(QPost.post.user)
        .fetchJoin()
        .where(dynamicLtId.and(QPost.post.isDeleted.eq(false)))
        .orderBy(QPost.post.id.desc())
        .limit(pageSearch.getSize())
        .fetch();
  }
}
