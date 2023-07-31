package com.blog.tanylog.config.security;

import com.blog.tanylog.user.controller.dto.SessionUser;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public class UserContext implements OAuth2User, Serializable {

  private final SessionUser sessionUser;
  private final Map<String, Object> attributes;

  public UserContext(SessionUser sessionUser, Map<String, Object> attributes) {
    this.sessionUser = sessionUser;
    this.attributes = attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> roles = new ArrayList<>();
    roles.add(new SimpleGrantedAuthority(sessionUser.getRole().getKey()));

    return roles;
  }

  /**
   * getAttributes(), getName() 은 OAuth2User 추상 메서드
   */

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public String getName() {
    return sessionUser.getUsername();
  }
}
