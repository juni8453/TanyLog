package com.blog.tanylog.config.oauth2.userinfo;

import java.util.Map;

public class KakaoUserInfo implements OAuthUserInfo {

  private final Map<String, Object> account;
  private Map<String, Object> profile;

  public KakaoUserInfo(Map<String, Object> account) {
    this.account = account;
    this.profile = (Map<String, Object>) account.get("profile");
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
