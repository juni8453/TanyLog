package com.blog.tanylog.user.repository;

import com.blog.tanylog.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByOauthId(String oauthId);
}
