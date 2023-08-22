package com.blog.tanylog.postLike.domain;

import com.blog.tanylog.post.domain.Post;
import com.blog.tanylog.user.domain.User;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post_like", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"post_id", "user_id"})})
public class PostLike {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  public void addUser(User user) {
    this.user = user;
  }

  public void addPost(Post post) {
    this.post = post;
  }

  public static PostLike createPostLike() {
    return new PostLike();
  }
}
