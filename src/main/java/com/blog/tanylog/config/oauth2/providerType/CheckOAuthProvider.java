package com.blog.tanylog.config.oauth2.providerType;

import com.blog.tanylog.config.oauth2.userinfo.OAuthUserInfo;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface CheckOAuthProvider {

  OAuthUserInfo getUserInfo(OAuth2User oAuth2User);
}
