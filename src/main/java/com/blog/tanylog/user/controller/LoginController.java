package com.blog.tanylog.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class LoginController {

  @GetMapping("/")
  public @ResponseBody
  String index() {
    return "index";
  }

  // 로그인 폼
  @GetMapping("/loginForm")
  public String loginForm() {
    return "loginForm";
  }
}