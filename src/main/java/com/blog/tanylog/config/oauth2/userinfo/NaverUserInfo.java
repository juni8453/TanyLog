package com.blog.tanylog.config.oauth2.userinfo;

import java.util.Map;

public class NaverUserInfo implements OAuthUserInfo {

  private final Map<String, Object> response;

  public NaverUserInfo(Map<String, Object> response) {
    this.response = response;
  }

  @Override
  public String oauthId() {
    return (String) response.get("id");
  }

  @Override
  public String username() {
    return (String) response.get("nickname");
  }

  @Override
  public String email() {
    return (String) response.get("email");
  }

  @Override
  public String profileImage() {
    return (String) response.get("profile_image");
  }
}
