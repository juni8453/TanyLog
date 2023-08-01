package com.blog.tanylog.post.repository;

import com.blog.tanylog.post.controller.dto.request.PageSearch;
import com.blog.tanylog.post.domain.Post;
import com.blog.tanylog.post.domain.QPost;
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
        .limit(pageSearch.getSize())
        .offset(pageSearch.getOffset())
        .orderBy(QPost.post.id.desc())
        .fetch();
  }
}
