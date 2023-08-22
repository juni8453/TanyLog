package com.blog.tanylog.postLike.service;

import com.blog.tanylog.config.security.UserContext;
import com.blog.tanylog.global.exception.domain.PostNotFound;
import com.blog.tanylog.global.exception.domain.UserNotFound;
import com.blog.tanylog.post.domain.Post;
import com.blog.tanylog.post.repository.PostRepository;
import com.blog.tanylog.postLike.controller.dto.request.PostLikeRequest;
import com.blog.tanylog.postLike.domain.PostLike;
import com.blog.tanylog.postLike.repository.PostLikeRepository;
import com.blog.tanylog.user.domain.User;
import com.blog.tanylog.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostLikeService {

  private final PostLikeRepository postLikeRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  @Transactional
  public void saveOrDelete(Long postId, UserContext userContext, PostLikeRequest request) {
    Long userId = userContext.getSessionUser().getUserId();

    Optional<PostLike> findPostLike = postLikeRepository.findPostLikeByPostIdAndUserId(
        postId, userId);

    // 클라이언트에서 실수할수도 있으니 이중 검증
    request.validate(findPostLike.orElse(null));

    User loginUser = userRepository.findById(userId)
        .orElseThrow(UserNotFound::new);

    Post findPost = postRepository.findById(postId)
        .orElseThrow(PostNotFound::new);

    if (request.isLiked()) {
      findPostLike.ifPresent(postLikeRepository::delete);

    } else {
      PostLike postLike = PostLike.createPostLike();
      postLike.addUser(loginUser);
      postLike.addPost(findPost);

      postLikeRepository.save(postLike);
    }
  }
}