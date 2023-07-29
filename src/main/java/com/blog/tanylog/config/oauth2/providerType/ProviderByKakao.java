package com.blog.tanylog.config.oauth2.providerType;

import com.blog.tanylog.config.oauth2.userinfo.KakaoUserInfo;
import com.blog.tanylog.config.oauth2.userinfo.OAuthUserInfo;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class ProviderByKakao implements CheckOAuthProvider {

  @Override
  public OAuthUserInfo getUserInfo(OAuth2User oAuth2User) {
    return new KakaoUserInfo(oAuth2User.getAttributes());
  }
}
