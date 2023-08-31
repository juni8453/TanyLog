//package com.blog.tanylog.post.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.blog.tanylog.post.controller.dto.request.PageSearch;
//import com.blog.tanylog.post.domain.Post;
//import com.blog.tanylog.post.repository.PostRepository;
//import java.util.List;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class PagingTest {
//
//  @Autowired
//  private PostRepository postRepository;
//
//  @Test
//  @DisplayName("Offset 방식으로 게시글 전체 조회 API 페이징 성능을 테스트합니다.")
//  void 게시글_전체조회_Offset() {
//    // given
//    PageSearch pageSearch = new PageSearch(null, 4000, 20, "", "");
//
//    // when
//    long startTime = System.currentTimeMillis();
//    List<Post> response = postRepository.readAll(pageSearch);
//    long endTime = System.currentTimeMillis();
//    long pagingTime = endTime - startTime;
//    System.out.println("반환 시간: " + pagingTime + "ms");
//
//    // then
//    assertThat(response).isNotNull();
//  }
//
//  @Test
//  @DisplayName("No - Offset 방식으로 게시글 전체 조회 API 페이징 성능을 테스트합니다.")
//  void 게시글_전체조회_No_Offset() {
//    // given
//    PageSearch pageSearch = new PageSearch(5000L, 1, 20, "", "");
//
//    // when
//    long startTime = System.currentTimeMillis();
//    List<Post> response = postRepository.readNoOffset(pageSearch);
//    long endTime = System.currentTimeMillis();
//    long pagingTime = endTime - startTime;
//    System.out.println("반환 시간: " + pagingTime + "ms");
//
//    // then
//    assertThat(response).isNotNull();
//  }
//}