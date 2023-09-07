package com.blog.tanylog.post.repository;

import com.blog.tanylog.config.security.UserContext;
import com.blog.tanylog.post.controller.dto.request.PageSearch;
import com.blog.tanylog.post.domain.Post;
import com.blog.tanylog.post.domain.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostCustomRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<Post> readAll(PageSearch pageSearch) {
    String searchType = pageSearch.getSearchType();
    String keyword = pageSearch.getKeyword();
    BooleanExpression searchPredicate = createSearchPredicate(searchType, keyword);

    // 커버링 인덱스를 활용해 조회 대상인 게시글 PK 조회 (서브 쿼리)
    List<Long> postIds = jpaQueryFactory.select(QPost.post.id)
        .from(QPost.post)
        .where(QPost.post.isDeleted.eq(false), searchPredicate)
        .orderBy(QPost.post.id.desc())
        .offset(pageSearch.getOffset())
        .limit(pageSearch.getSize())
        .fetch();

    // 대상 게시글이 없는 경우 추가 쿼리 수행 방지를 위해 바로 반환
    if (CollectionUtils.isEmpty(postIds)) {
      return new ArrayList<>();
    }

    // in 절은 정렬을 보장하지 않기 때문에 order by 필요
    return jpaQueryFactory.selectFrom(QPost.post)
        .join(QPost.post.user).fetchJoin()
        .where(QPost.post.id.in(postIds))
        .orderBy(QPost.post.id.desc())
        .fetch();
  }

  private BooleanExpression createSearchPredicate(String searchType, String keyword) {
    BooleanExpression predicate = null; // 기본적으로 검색 결과 없음

    if ("TITLE".equalsIgnoreCase(searchType)) {
      predicate = createTitlePredicate(keyword);

    } else if ("CONTENT".equalsIgnoreCase(searchType)) {
      predicate = createContentPredicate(keyword);

    } else if ("USER".equalsIgnoreCase(searchType)) {
      predicate = createUserPredicate(keyword);
    }

    return predicate;
  }

  private BooleanExpression createUserPredicate(String usernamePattern) {
    if (!StringUtils.hasText(usernamePattern)) {
      return null; // 검색 조건이 없으면 null 반환
    }

    return QPost.post.user.name.like(usernamePattern + "%");
  }

  private BooleanExpression createTitlePredicate(String titlePattern) {
    if (!StringUtils.hasText(titlePattern)) {
      return null; // 검색 조건이 없으면 null 반환
    }

    return QPost.post.title.like(titlePattern + "%");
  }

  private BooleanExpression createContentPredicate(String contentPattern) {
    if (!StringUtils.hasText(contentPattern)) {
      return null; // 검색 조건이 없으면 null 반환
    }

    return QPost.post.content.like(contentPattern + "%");
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

  @Override
  public List<Post> readMyPosts(PageSearch pageSearch, UserContext userContext) {
    return jpaQueryFactory.selectFrom(QPost.post)
        .join(QPost.post.user)
        .fetchJoin()
        .where(QPost.post.isDeleted.eq(false),
            QPost.post.user.id.eq(userContext.getSessionUser().getUserId()))
        .limit(pageSearch.getSize())
        .offset(pageSearch.getOffset())
        .orderBy(QPost.post.id.desc())
        .fetch();
  }
}
