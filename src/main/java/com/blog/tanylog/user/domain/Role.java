package com.blog.tanylog.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

  ADMIN("ROLE_ADMIN", "관리자 권한 계정"),
  USER("ROLE_USER", "일반 권한 계쩡");

  private final String key;
  private final String title;

}
