package com.blog.tanylog.post.service;

import com.blog.tanylog.config.security.UserContext;
import com.blog.tanylog.global.exception.domain.OtherUserDeleteException;
import com.blog.tanylog.global.exception.domain.OtherUserUpdateException;
import com.blog.tanylog.global.exception.domain.PostNotFound;
import com.blog.tanylog.global.exception.domain.UserNotFound;
import com.blog.tanylog.post.controller.dto.request.PostSaveRequest;
import com.blog.tanylog.post.controller.dto.request.PostUpdateRequest;
import com.blog.tanylog.post.controller.dto.response.PostSingleReadResponse;
import com.blog.tanylog.post.controller.dto.response.WriterResponse;
import com.blog.tanylog.post.domain.Post;
import com.blog.tanylog.post.repository.PostRepository;
import com.blog.tanylog.user.domain.User;
import com.blog.tanylog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;

  @Transactional
  public void save(UserContext userContext, PostSaveRequest request) {
    Long userId = userContext.getSessionUser().getUserId();
    User loginUser = userRepository.findById(userId)
        .orElseThrow(UserNotFound::new);

    String title = request.getTitle();
    String content = request.getContent();
    boolean isDeleted = request.isDeleted();

    Post post = request.toEntity(title, content, isDeleted);
    post.addUser(loginUser);

    postRepository.save(post);
  }

  @Transactional
  public void delete(Long postId, UserContext userContext) {
    Long userId = userContext.getSessionUser().getUserId();
    User loginUser = userRepository.findById(userId)
        .orElseThrow(UserNotFound::new);

    Post findPost = postRepository.findById(postId)
        .orElseThrow(PostNotFound::new);

    if (!findPost.checkUser(loginUser)) {
      throw new OtherUserDeleteException();
    }

    postRepository.delete(findPost);
  }

  @Transactional
  public void update(Long postId, UserContext userContext, PostUpdateRequest request) {
    Long userId = userContext.getSessionUser().getUserId();
    User loginUser = userRepository.findById(userId)
        .orElseThrow(UserNotFound::new);

    Post findPost = postRepository.findById(postId)
        .orElseThrow(PostNotFound::new);

    if (!findPost.checkUser(loginUser)) {
      throw new OtherUserUpdateException();
    }

    String updateTitle = request.getTitle();
    String updateContent = request.getContent();

    findPost.updatePost(updateTitle, updateContent);
  }

  // GET Method 에 Transactional 을 꼭 사용해야할까 ?
  @Transactional
  public PostSingleReadResponse read(Long postId) {
    Post findPost = postRepository.findById(postId)
        .orElseThrow(PostNotFound::new);

    User user = findPost.getUser();

    WriterResponse writer = WriterResponse.builder()
        .name(user.getName())
        .email(user.getEmail())
        .picture(user.getPicture())
        .build();

    return PostSingleReadResponse.builder()
        .id(findPost.getId())
        .title(findPost.getTitle())
        .content(findPost.getContent())
        .createdDate(findPost.getCreateAt())
        .modifiedDate(findPost.getModifiedAt())
        .writer(writer)
        .build();
  }
}
