package com.blog.tanylog.config;

import com.blog.tanylog.config.security.UserContext;
import com.blog.tanylog.user.controller.dto.SessionUser;
import com.blog.tanylog.user.domain.User;
import com.blog.tanylog.user.repository.UserRepository;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements
    WithSecurityContextFactory<WithMockCustomUser> {

  private final UserRepository userRepository;
  private User user;

  public WithMockCustomUserSecurityContextFactory(
      UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Optional<User> findUser = userRepository.findByOauthId(annotation.oauthId());

    if (findUser.isEmpty()) {
      user = User.builder()
          .oauthId(annotation.oauthId())
          .name(annotation.username())
          .email(annotation.email())
          .picture(annotation.picture())
          .role(annotation.role())
          .build();

      userRepository.save(user);
    }

    findUser.ifPresent(value -> user = value);

    SessionUser sessionUser = SessionUser.builder()
        .userId(user.getId())
        .oauthId(user.getOauthId())
        .username(user.getName())
        .email(user.getEmail())
        .picture(user.getPicture())
        .role(user.getRole())
        .build();

    UserContext userContext = new UserContext(sessionUser, null);

    OAuth2AuthenticationToken oAuth2AuthenticationToken = new OAuth2AuthenticationToken(userContext,
        userContext.getAuthorities(), annotation.oauthId());

    context.setAuthentication(oAuth2AuthenticationToken);

    return context;
  }
}
