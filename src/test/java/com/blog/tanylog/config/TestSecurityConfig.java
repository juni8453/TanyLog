package com.blog.tanylog.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // notion 에 기록
    http.csrf().disable()
        .headers().frameOptions().disable()
        .and()

        .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/posts/**").hasAnyRole("ADMIN", "USER")
        .antMatchers(HttpMethod.DELETE, "/posts/**").hasAnyRole("ADMIN", "USER")
        .antMatchers(HttpMethod.PUT, "/posts/**").hasAnyRole("ADMIN", "USER")

        .antMatchers(HttpMethod.DELETE, "/comments/**").hasAnyRole("ADMIN", "USER")
        .anyRequest().permitAll()

        .and()
        .logout()
        .logoutSuccessUrl("/")

        .and()
        .oauth2Login()
        .loginPage("/loginForm");

    return http.build();
  }
}