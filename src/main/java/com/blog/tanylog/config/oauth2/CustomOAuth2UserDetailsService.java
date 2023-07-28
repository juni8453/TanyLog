package com.blog.tanylog.config.oauth2;

import com.blog.tanylog.config.oauth2.factory.OAuthFactory;
import com.blog.tanylog.config.oauth2.providerType.CheckOAuthProvider;
import com.blog.tanylog.config.oauth2.userinfo.OAuthUserInfo;
import com.blog.tanylog.user.controller.dto.SessionUser;
import com.blog.tanylog.user.domain.Role;
import com.blog.tanylog.user.domain.User;
import com.blog.tanylog.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserDetailsService implements
    OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserRepository userRepository;
  private User user;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);
    String providerName = userRequest.getClientRegistration().getRegistrationId();

    // Naver + Kakao 리팩토링
    CheckOAuthProvider oAuth2Provider = new OAuthFactory().getProvider(providerName);
    OAuthUserInfo userInfo = oAuth2Provider.getUserInfo(oAuth2User);

    String username = userInfo.username();
    String profileImage = userInfo.profileImage();
    String email = userInfo.email();
    Role role = Role.USER;

    // 가입된 유저인지 확인
    Optional<User> findUser = userRepository.findByEmail(email);

    if (findUser.isEmpty()) {
      user = saveUserInfo(username, email, profileImage, role);
    }

    findUser.ifPresent(value -> user = value);

    SessionUser sessionUser = SessionUser.builder()
        .username(username)
        .email(email)
        .picture(profileImage)
        .role(role)
        .build();

    return new UserContext(sessionUser, oAuth2User.getAttributes());
  }

  private User saveUserInfo(String username, String email, String profileImage, Role role) {
    User user = User.builder()
        .name(username)
        .email(email)
        .picture(profileImage)
        .role(role)
        .build();

    userRepository.save(user);

    return user;
  }
}
