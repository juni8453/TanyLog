package com.blog.tanylog.post.controller;

import com.blog.tanylog.config.security.UserContext;
import com.blog.tanylog.post.controller.dto.request.PageSearch;
import com.blog.tanylog.post.controller.dto.request.PostSaveRequest;
import com.blog.tanylog.post.controller.dto.request.PostUpdateRequest;
import com.blog.tanylog.post.controller.dto.response.PostMultiReadResponse;
import com.blog.tanylog.post.controller.dto.response.PostSingleReadResponse;
import com.blog.tanylog.post.service.PostService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PostController {

  private final PostService postService;

  @PostMapping("/posts")
  public ResponseEntity<Void> save(@Valid @RequestBody PostSaveRequest request,
      @AuthenticationPrincipal UserContext userContext) {
    Long savedPostId = postService.save(userContext, request);

    URI location = URI.create("/posts/" + savedPostId);

    return ResponseEntity.created(location).build();
  }

  @DeleteMapping("/posts/{postId}")
  public void delete(@PathVariable Long postId, @AuthenticationPrincipal UserContext userContext) {
    postService.delete(postId, userContext);
  }

  @PutMapping("/posts/{postId}")
  public void update(@PathVariable Long postId, @Valid @RequestBody PostUpdateRequest request,
      @AuthenticationPrincipal UserContext userContext) {

    postService.update(postId, userContext, request);
  }

  @GetMapping("/posts/{postId}")
  public ResponseEntity<PostSingleReadResponse> read(@PathVariable Long postId,
      @AuthenticationPrincipal UserContext userContext) {
    PostSingleReadResponse response = postService.read(postId, userContext);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/posts")
  public ResponseEntity<PostMultiReadResponse> readAll(@ModelAttribute PageSearch pageSearch) {
    PostMultiReadResponse response = postService.readAll(pageSearch);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/posts/my_posts")
  public ResponseEntity<PostMultiReadResponse> readMyPosts(@ModelAttribute PageSearch pageSearch,
      @AuthenticationPrincipal UserContext userContext) {
    PostMultiReadResponse response = postService.readMyPosts(pageSearch, userContext);

    return ResponseEntity.ok(response);
  }
}
