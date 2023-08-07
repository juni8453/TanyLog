package com.blog.tanylog.config;

import com.blog.tanylog.user.domain.Role;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

  String oauthId() default "test_oauthId";

  String username() default "test_user";

  String email() default "test_email";

  String picture() default "test_picture";

  Role role() default Role.USER;
}
