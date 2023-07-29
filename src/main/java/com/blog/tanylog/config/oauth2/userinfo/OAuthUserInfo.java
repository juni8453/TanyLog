package com.blog.tanylog.config.oauth2.userinfo;

public interface OAuthUserInfo {

  String oauthId();

  String username();

  String email();

  String profileImage();
}
