//package com.blog.tanylog.config;
//
//import com.blog.tanylog.config.security.UserContext;
//import com.blog.tanylog.user.controller.dto.SessionUser;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.test.context.support.WithSecurityContextFactory;
//
//public class WithMockCustomUserSecurityContextFactory implements
//    WithSecurityContextFactory<WithMockCustomUser> {
//
//  @Override
//  public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
//    SecurityContext context = SecurityContextHolder.createEmptyContext();
//    SessionUser sessionUser = SessionUser.builder()
//        .userId(annotation.userId())
//        .oauthId(annotation.oauthId())
//        .username(annotation.username())
//        .email(annotation.email())
//        .picture(annotation.picture())
//        .role(annotation.role())
//        .build();
//
//    UserContext userContext = new UserContext(sessionUser, null);
//
//    OAuth2AuthenticationToken oAuth2AuthenticationToken = new OAuth2AuthenticationToken(userContext,
//        userContext.getAuthorities(), annotation.oauthId());
//
//    context.setAuthentication(oAuth2AuthenticationToken);
//
//    return context;
//  }
//}
