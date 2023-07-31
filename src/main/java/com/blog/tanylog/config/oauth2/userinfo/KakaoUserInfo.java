package com.blog.tanylog.config.oauth2.userinfo;

import java.util.Map;

public class KakaoUserInfo implements OAuthUserInfo {

  private final Map<String, Object> attributes;
  private final Map<String, Object> account;
  private final Map<String, Object> profile;

  public KakaoUserInfo(Map<String, Object> attributes) {
    this.attributes = attributes;
    this.account = (Map<String, Object>) attributes.get("kakao_account");
    this.profile = (Map<String, Object>) account.get("profile");
  }

  @Override
  public String oauthId() {
    return attributes.get("id").toString();
  }

  @Override
  public String username() {
    return (String) profile.get("nickname");
  }

  @Override
  public String email() {
    return (String) account.get("email");
  }

  @Override
  public String profileImage() {
    return (String) profile.get("profile_image_url");
  }
}
