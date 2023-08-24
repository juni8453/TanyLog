package com.blog.tanylog.post.repository;

import com.blog.tanylog.config.security.UserContext;
import com.blog.tanylog.post.controller.dto.request.PageSearch;
import com.blog.tanylog.post.domain.Post;
import com.blog.tanylog.post.domain.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostCustomRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<Post> readAll(PageSearch pageSearch) {
    String searchType = pageSearch.getSearchType();
    String keyword = pageSearch.getKeyword();
    BooleanExpression searchPredicate = createSearchPredicate(searchType, keyword);

    /**
     * 정확한 Paging 을 위해 검색 쿼리와 페이징 쿼리 분리
     */
    JPAQuery<Post> searchQuery = jpaQueryFactory.selectFrom(QPost.post)
        .join(QPost.post.user)
        .fetchJoin()
        .where(QPost.post.isDeleted.eq(false), searchPredicate);

    return searchQuery
        .limit(pageSearch.getSize())
        .offset(pageSearch.getOffset())
        .orderBy(QPost.post.id.desc())
        .fetch();
  }

  private BooleanExpression createSearchPredicate(String searchType, String keyword) {
    BooleanExpression predicate = null;

    if ("TITLE".equalsIgnoreCase(searchType)) {
      predicate = createTitlePredicate(keyword);

    } else if ("CONTENT".equalsIgnoreCase(searchType)) {
      predicate = createContentPredicate(keyword);

    } else if ("USER".equalsIgnoreCase(searchType)) {
      predicate = createUserPredicate(keyword);
    }

    return predicate;
  }

  private BooleanExpression createUserPredicate(String keyword) {
    if (!StringUtils.hasText(keyword)) {
      return null;
    }

    return QPost.post.user.name.eq(keyword);
  }

  private BooleanExpression createTitlePredicate(String keyword) {
    if (!StringUtils.hasText(keyword)) {
      return null;
    }

    return QPost.post.title.containsIgnoreCase(keyword);
  }

  private BooleanExpression createContentPredicate(String keyword) {
    if (!StringUtils.hasText(keyword)) {
      return null;
    }

    return QPost.post.content.containsIgnoreCase(keyword);
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
