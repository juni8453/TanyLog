package com.blog.tanylog.user.controller.dto;

import com.blog.tanylog.user.domain.Role;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SessionUser implements Serializable {

  private String oauthId;
  private String username;
  private String email;
  private String picture;
  private Role role;

  @Builder
  public SessionUser(String oauthId, String username, String email, String picture, Role role) {
    this.oauthId = oauthId;
    this.username = username;
    this.email = email;
    this.picture = picture;
    this.role = role;
  }
}
