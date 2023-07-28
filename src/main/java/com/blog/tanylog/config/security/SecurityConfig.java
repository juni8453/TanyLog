package com.blog.tanylog.config.security;

import com.blog.tanylog.config.oauth2.CustomOAuth2UserDetailsService;
import com.blog.tanylog.user.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomOAuth2UserDetailsService customOAuth2UserService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // notion 에 기록
    http.csrf().disable()
        .headers().frameOptions().disable()
        .and()

        .authorizeRequests()
        .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/loginForm")
        .permitAll()
        .antMatchers("/api/**").hasRole(Role.ADMIN.name())
        .antMatchers("/api/**").hasRole(Role.USER.name())
        .anyRequest().authenticated()

        .and()
        .logout()
        .logoutSuccessUrl("/")

        .and()
        .oauth2Login()
        .loginPage("/loginForm")
        .userInfoEndpoint()
        .userService(customOAuth2UserService);

    return http.build();
  }
}
