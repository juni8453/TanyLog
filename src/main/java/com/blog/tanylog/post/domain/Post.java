package com.blog.tanylog.post.domain;

import com.blog.tanylog.common.BaseEntity;
import com.blog.tanylog.user.domain.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 50, nullable = false)
  private String title;

  @Lob
  @Column(nullable = false)
  private String content;

  private boolean isDeleted;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Builder
  public Post(String title, String content, boolean isDeleted) {
    this.title = title;
    this.content = content;
    this.isDeleted = isDeleted;
  }

  public void addUser(User user) {
    this.user = user;
  }
}
