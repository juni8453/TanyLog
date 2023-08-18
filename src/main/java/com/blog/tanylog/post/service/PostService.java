package com.blog.tanylog.post.service;

import com.blog.tanylog.config.security.UserContext;
import com.blog.tanylog.global.exception.domain.OtherUserDeleteException;
import com.blog.tanylog.global.exception.domain.OtherUserUpdateException;
import com.blog.tanylog.global.exception.domain.PostNotFound;
import com.blog.tanylog.global.exception.domain.UserNotFound;
import com.blog.tanylog.post.controller.dto.request.PageSearch;
import com.blog.tanylog.post.controller.dto.request.PostSaveRequest;
import com.blog.tanylog.post.controller.dto.request.PostUpdateRequest;
import com.blog.tanylog.post.controller.dto.response.PostMultiReadResponse;
import com.blog.tanylog.post.controller.dto.response.PostSingleReadResponse;
import com.blog.tanylog.post.controller.dto.response.PostWriterResponse;
import com.blog.tanylog.post.domain.Post;
import com.blog.tanylog.post.repository.PostRepository;
import com.blog.tanylog.postLike.repository.PostLikeRepository;
import com.blog.tanylog.user.domain.User;
import com.blog.tanylog.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final PostLikeRepository postLikeRepository;

  @Transactional
  public Long save(UserContext userContext, PostSaveRequest request) {
    Long userId = userContext.getSessionUser().getUserId();
    User loginUser = userRepository.findById(userId)
        .orElseThrow(UserNotFound::new);

    String title = request.getTitle();
    String content = request.getContent();
    boolean isDeleted = request.isDeleted();

    Post post = request.toEntity(title, content, isDeleted);
    post.addUser(loginUser);

    Post savedPost = postRepository.save(post);

    return savedPost.getId();
  }

  @Transactional
  public void delete(Long postId, UserContext userContext) {
    Long userId = userContext.getSessionUser().getUserId();
    User loginUser = userRepository.findById(userId)
        .orElseThrow(UserNotFound::new);

    Post findPost = postRepository.findByPostId(postId)
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

    Post findPost = postRepository.findByPostId(postId)
        .orElseThrow(PostNotFound::new);

    if (!findPost.checkUser(loginUser)) {
      throw new OtherUserUpdateException();
    }

    String updateTitle = request.getTitle();
    String updateContent = request.getContent();

    findPost.updatePost(updateTitle, updateContent);
  }

  /**
   * GET Method 에 Transactional 을 꼭 사용해야할까 ? 선언적 트랜잭션이 없더라도 단순 조회는 가능하나, 영속성 컨텍스트가 생성되지 않아 관리를 받지
   * 못하기 때문에 Lazy Loading 관련 예외가 발생할 수 있다. 물론 findPostId() 는 FETCH JOIN 을 사용하고 있기 때문에 한번에 User 데이터까지
   * 조회하기 때문에 Lazy Loading 예외는 발생하지 않는다.
   */
  /**
   * 로그인 상태에서 조회라면 해당 유저가 post ID 게시글을 좋아요 했는지 안했는지에 대한 판단이 필요합니다. 좋아요 객체가 존재한다면 이미 좋아요 한 상태, 존재하지
   * 않다면 좋아요를 하지 않은 상태입니다. 비로그인 상태에서 조회라면 isLiked = false 로 응답합니다.
   */
  @Transactional(readOnly = true)
  public PostSingleReadResponse read(Long postId, UserContext userContext) {
    boolean isLiked = false;

    if (userContext != null) {
      isLiked = postLikeRepository.findPostLikeByPostIdAndUserId(postId,
          userContext.getSessionUser().getUserId()).isPresent();
    }

    Post findPost = postRepository.findByPostId(postId)
        .orElseThrow(PostNotFound::new);

    User user = findPost.getUser();
    PostWriterResponse writer = PostWriterResponse.builder()
        .name(user.getName())
        .email(user.getEmail())
        .picture(user.getPicture())
        .build();

    return PostSingleReadResponse.builder()
        .id(findPost.getId())
        .title(findPost.getTitle())
        .content(findPost.getContent())
        .isLiked(isLiked)
        .createdDate(findPost.getCreateAt())
        .modifiedDate(findPost.getModifiedAt())
        .writer(writer)
        .build();
  }

  @Transactional(readOnly = true)
  public PostMultiReadResponse readAll(PageSearch pageSearch) {
    List<Post> offset = postRepository.readAll(pageSearch);

    List<PostSingleReadResponse> response = offset.stream()
        .map(post -> PostSingleReadResponse.builder()
            .id(post.getId())
            .title(post.getTitle())
            .content(post.getContent())
            .createdDate(post.getCreateAt())
            .modifiedDate(post.getModifiedAt())
            .writer(PostWriterResponse.builder()
                .name(post.getUser().getName())
                .email(post.getUser().getEmail())
                .picture(post.getUser().getPicture())
                .build())
            .build())
        .collect(Collectors.toList());

    return PostMultiReadResponse
        .builder()
        .postsResponse(response)
        .build();
  }
}
